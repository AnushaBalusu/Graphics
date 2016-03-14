//
//  Rasterizer.java
//
//  Created by Joe Geigel on 1/21/10.
//  Copyright 2010 Rochester Institute of Technology. All rights reserved.
//
//  Contributor:  ANUSHA BALUSU
//

///
// 
// A simple class for performing rasterization algorithms.
//
///

import java.util.*;

public class Rasterizer {

   ///
   // number of scanlines
   ///

   int n_scanlines;

   ///
   // Constructor
   //
   // @param n number of scanlines
   //
   ///

   Rasterizer (int n)
   {
      n_scanlines = n;
   }

   ///
   // Draw a line from (x0,y0) to (x1, y1) on the simpleCanvas C.
   //
   // Implementation should be using the Midpoint Method
   //
   // You are to add the implementation here using only calls
   // to C.setPixel()
   //
   // @param x0 x coord of first endpoint
   // @param y0 y coord of first endpoint
   // @param x1 x coord of second endpoint
   // @param y1 y coord of second endpoint
   // @param C  The canvas on which to apply the draw command.
   ///

   public void drawLine (int x0, int y0, int x1, int y1, simpleCanvas C)
   {
      // YOUR IMPLEMENTATION GOES HERE
      int dx = x1 - x0;
      int dy = y1 - y0;
      int absDx = ((x0 - x1) > 0) ? (x0 - x1) : (x1 - x0);
      int absDy = ((y0 - y1) > 0) ? (y0 - y1) : (y1 - y0);
      int p = 0;
      int xtemp = 0;
      int ytemp = 0;

      if(absDx == 0) {
         if( y1 < y0) {
            ytemp = y1;               // put y min in y0
            y1 = y0;
            y0 = ytemp;
         }
         C.setPixel(x0, y0);
         while(y0 != y1) {
            y0++;
            C.setPixel(x0, y0);
         }
      }else if(absDy == 0) {          // horizontal lines
         if( x1 < x0) {               // put x min in x0
            xtemp = x1;
            x1 = x0;
            x0 = xtemp;
         }
         C.setPixel(x0, y0);
         while(x0 != x1) {
            x0++;
            C.setPixel(x0, y0);
         }
      }else if(absDx == absDy) {         // 45 or 135 degree 
         if( x1 < x0) {                  // put x min in x0
            xtemp = x1;
            x1 = x0;
            x0 = xtemp;
            ytemp = y1;                  // and its y value in y0
            y1 = y0;
            y0 = ytemp;
         }
         C.setPixel(x0, y0);
         if( dx * dy > 0 ) {           // 45 degree slope
            while(x0 != x1) {
               x0++;
               y0++;
               C.setPixel(x0, y0);
            }
         }else {                       // 135 degree slope
            while(x0 != x1) {
               x0++;
               y0--;
               C.setPixel(x0, y0);
            }
         }

      }else if(absDx > absDy) {           // Q1, Q4, Q5, Q8 octants
         if(x0 > x1) {              // put x min in x0
            xtemp = x1;
            x1 = x0;
            x0 = xtemp;

            ytemp = y1;             // and its corresponding y value in y0
            y1 = y0;
            y0 = ytemp;
         }
         p = 2 * absDy - absDx;
         if(dx * dy > 0) {          // Q1, Q5 octants
            C.setPixel(x0,y0);
            while(x0 < x1) {
               x0++;
               if(p <= 0) {
                  p += 2*absDy;
               }else {
                  y0++;
                  p += (2*absDy) - (2*absDx);
               }
               C.setPixel(x0,y0);
            }
         }else {                    // Q4, Q8 octants
            C.setPixel(x0,y0);
            while(x0 < x1) {
               x0++;
               if(p <= 0) {
                  p += 2*absDy;
               }else {
                  y0--;
                  p += (2*absDy) - (2*absDx);
               }
               C.setPixel(x0,y0);
            }
         }
      }else {                       // Q2, Q3, Q6, Q7 octants
         if(y0 > y1) {              // put y min in y0
            xtemp = x1;
            x1 = x0;
            x0 = xtemp;

            ytemp = y1;             // and its corresponding x value in x0
            y1 = y0;
            y0 = ytemp;
         }
         p = 2 * absDx - absDy;
         if(dx * dy > 0) {          // Q2, Q6 octants
            C.setPixel(x0,y0);
            while(y0 < y1) {
               y0++;
               if(p <= 0) {
                  p += 2*absDx;
               }else {
                  x0++;
                  p += (2*absDx) - (2*absDy);
               }
               C.setPixel(x0,y0);
            }
         }else {                    // Q3, Q7 octants
            C.setPixel(x0,y0);
            while(y0 < y1) {
               y0++;
               if(p <= 0) {
                  p += 2*absDx;
               }else {
                  x0--;
                  p += (2*absDx) - (2*absDy);
               }
               C.setPixel(x0,y0);
            }
         }
      }//End else block of Q2, Q3, Q6, Q7 octants

   }
}
