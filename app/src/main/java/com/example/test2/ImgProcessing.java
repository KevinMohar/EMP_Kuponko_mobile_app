package com.example.test2;

import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;

import org.opencv.android.Utils;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.Feature2D;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.ORB;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ImgProcessing {

    public Bitmap detectEdges(Bitmap bitmap, int treshA, int treshB){
        Mat frame = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1);

        Utils.bitmapToMat(bitmap,frame);

        // convert image to grayscale color format
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2GRAY);

        // use Canny algorithm to detect edges
        Imgproc.Canny(frame, frame, treshA, treshB);

        Utils.matToBitmap(frame,bitmap);
        return bitmap;
    }

    public Mat getHoughPTransform(Bitmap bitmap, double rho, double theta, int threshold) {

        Mat image = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1);
        Mat result = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1);

        Utils.bitmapToMat(bitmap,image);
        Utils.bitmapToMat(bitmap,result);

        Mat lines = new Mat();
        Imgproc.HoughLinesP(image, lines, rho, theta, threshold);

        for (int i = 0; i < lines.cols(); i++) {
            double[] val = lines.get(0, i);
            Imgproc.line(result, new Point(val[0], val[1]), new Point(val[2], val[3]), new Scalar(0, 0, 255), 2);
        }
        return result;
    }

    public Bitmap lineDetect(Bitmap bitmap, Bitmap horizontalLines, double rho, double theta, int threshold, int minLineSize, int lineGap){
        Mat displayimg = new Mat();
        Mat mat = new Mat();
        Mat edges = new Mat();
        Mat lines = new Mat();

        Utils.bitmapToMat(horizontalLines, mat);
        Utils.bitmapToMat(bitmap, displayimg);
        Imgproc.GaussianBlur(mat,edges, new Size(3,3), 20);
        Imgproc.Canny(mat, edges, 50, 90);

        Imgproc.HoughLinesP(edges, lines, rho, theta,threshold,minLineSize,lineGap);

        for (int x = 0; x < lines.rows(); x++) {
            double[] vec = lines.get(x, 0);
            double x1 = vec[0],
                    y1 = vec[1],
                    x2 = vec[2],
                    y2 = vec[3];
            Point start = new Point(x1, y1);
            Point end = new Point(x2, y2);

            Imgproc.line(displayimg, start, end, new Scalar(255, 0, 0, 255), 15);

        }

        Utils.matToBitmap(displayimg, bitmap);
        return bitmap;
    }

    public Bitmap horizontalLineDetect(Bitmap bitmap){

        // convert bitmap to mat image
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap, src);

        // transform image to grayscale
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

        // apply adaptive treshold at the bitwise_not of gray
        Mat bw = new Mat();
        Core.bitwise_not(gray, gray);
        Imgproc.adaptiveThreshold(gray, bw, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, -2);

        // Create the images that will use to extract the horizontal and vertical lines
        Mat horizontal = bw.clone();

        // Specify size on horizontal and vertical axis
        int horizontal_size = horizontal.cols() / 30;

        // Create structure element for extracting horizontal and vertical lines through morphology operations
        Mat horizontalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(horizontal_size,1));

        // Apply morphology operations
        Imgproc.erode(horizontal, horizontal, horizontalStructure);
        Imgproc.dilate(horizontal, horizontal, horizontalStructure);


        Utils.matToBitmap(horizontal, bitmap);
        return bitmap;
    }

    public MatOfKeyPoint computeImageKeyPoints(Mat photo){

        // copy original image
        Mat imageCopy = photo.clone();

        // create ORB element
        ORB orb = ORB.create(15000);

        // keypoints and descriptors
        MatOfKeyPoint keyPoint = new MatOfKeyPoint();
        Mat descriptors = new Mat();

        // detect and compute keypoints
        orb.detectAndCompute(imageCopy, new Mat(), keyPoint, descriptors);

        /**
         // save image with keypoints (red color) to keyedImage
         Mat keyedImage = originalImage.clone();
         Scalar pointColor = new Scalar(255,0,0); // red color
         Features2d.drawKeypoints(keyedImage, keyPoint, keyedImage, pointColor, 3);
         **/

        return keyPoint;
    }

    public Bitmap matchImages(Bitmap templateBitmap, Bitmap bitmap){

        // bitmap to map format
        Mat originalImage = new Mat(); // img taken by camera
        Mat templateCopy = new Mat();  // template of recipt
        Utils.bitmapToMat(bitmap, originalImage);
        Utils.bitmapToMat(templateBitmap, templateCopy);

        // orb element
        ORB orb = ORB.create(1500);

        // keypoints and descriptors for camera image
        MatOfKeyPoint keyPointOriginal = new MatOfKeyPoint();
        Mat descriptorsOriginal = new Mat();

        // keypoints and descriptors of template image
        MatOfKeyPoint keyPointTemplate = new MatOfKeyPoint();
        Mat descriptorsTemplate = new Mat();

        // compute keypoints and descriptors for both images
        orb.detectAndCompute(originalImage, new Mat(), keyPointOriginal, descriptorsOriginal);
        orb.detectAndCompute(templateCopy, new Mat(), keyPointTemplate, descriptorsTemplate);

        // image matcher
        BFMatcher bf = new BFMatcher(BFMatcher.BRUTEFORCE_HAMMING);
        List<MatOfDMatch> matches = new ArrayList<MatOfDMatch>();
        int numOfBestMatches = 5;
        bf.knnMatch(descriptorsOriginal, descriptorsTemplate, matches, numOfBestMatches);

        // ratio test
        LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
        for (Iterator<MatOfDMatch> iterator = matches.iterator(); iterator.hasNext();) {
            MatOfDMatch matOfDMatch = (MatOfDMatch) iterator.next();
            if (matOfDMatch.toArray()[0].distance / matOfDMatch.toArray()[1].distance < 0.9) {
                good_matches.add(matOfDMatch.toArray()[0]);
            }
        }

        // get keypoint coordinates of good matches to find homography and remove outliers using ransac
        List<Point> pts1 = new ArrayList<Point>();
        List<Point> pts2 = new ArrayList<Point>();
        for(int i = 0; i<good_matches.size(); i++){
            pts1.add(keyPointOriginal.toList().get(good_matches.get(i).queryIdx).pt);
            pts2.add(keyPointTemplate.toList().get(good_matches.get(i).trainIdx).pt);
        }

        // convertion of data types - there is maybe a more beautiful way
        Mat outputMask = new Mat();
        MatOfPoint2f pts1Mat = new MatOfPoint2f();
        pts1Mat.fromList(pts1);
        MatOfPoint2f pts2Mat = new MatOfPoint2f();
        pts2Mat.fromList(pts2);

        Mat Homog = Calib3d.findHomography(pts1Mat, pts2Mat, Calib3d.RANSAC, 15, outputMask, 2000, 0.995);

        // outputMask contains zeros and ones indicating which matches are filtered
        LinkedList<DMatch> better_matches = new LinkedList<DMatch>();
        for (int i = 0; i < good_matches.size(); i++) {
            if (outputMask.get(i, 0)[0] != 0.0) {
                better_matches.add(good_matches.get(i));
            }
        }

        // DRAWING OUTPUT
        Mat outputImg = new Mat();
        // this will draw all matches, works fine
        MatOfDMatch better_matches_mat = new MatOfDMatch();
        better_matches_mat.fromList(better_matches);
        Features2d.drawMatches(originalImage, keyPointOriginal, templateCopy, keyPointTemplate, better_matches_mat, outputImg);

        Utils.matToBitmap(originalImage, bitmap);
        return bitmap;
    }
}