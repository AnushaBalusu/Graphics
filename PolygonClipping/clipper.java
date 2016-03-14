//
//  Clipper.java
//
//  Created by Joe Geigel on 1/21/10.
//  Copyright 2010 Rochester Institute of Technology. All rights reserved.
//
//  Contributor:  ANUSHA BALUSU
//

///
// Object for performing clipping
//
///
import java.util.*;

public class clipper {

   /**
    * Vertex Class for co-ordinate (x,y) 
    */
   public class Vertex {
      float x;
      float y;

      public Vertex(float x, float y) {
         this.x = x;
         this.y = y;
      }
   }

   /**
    * Edge class for clip boundary, vertex 1 and 2 are two ends of an edge
    */
   public class Edge {
      Vertex vertex1;
      Vertex vertex2;

      public Edge(float x1, float y1, float x2, float y2) {
         this.vertex1 = new Vertex(x1, y1);
         this.vertex2 = new Vertex(x2, y2);
      }
   }
   ///
   // clipPolygon
   //
   // Clip the polygon with vertex count in and vertices inx/iny
   // against the rectangular clipping region specified by lower-left corner
   // (llx,lly) and upper-right corner (urx,ury). The resulting vertices are
   // placed in outx/outy.
   //
   // The routine should return the the vertex count of the polygon
   // resulting from the clipping.
   //
   // @param in the number of vertices in the polygon to be clipped
   // @param inx - x coords of vertices of polygon to be clipped.
   // @param iny - y coords of vertices of polygon to be clipped.
   // @param outx - x coords of vertices of polygon resulting after clipping.
   // @param outy - y coords of vertices of polygon resulting after clipping.
   // @param llx - x coord of lower left of clipping rectangle.
   // @param lly - y coord of lower left of clipping rectangle.
   // @param urx - x coord of upper right of clipping rectangle.
   // @param ury - y coord of upper right of clipping rectangle.
   //
   // @return number of vertices in the polygon resulting after clipping
   //
   ///
   public int clipPolygon(int in, float inx[], float iny[],
         float outx[], float outy[],
         float llx, float lly, float urx, float ury)
   {
      // YOUR IMPLEMENTATION GOES HERE
      ArrayList<Vertex> inVertices = new ArrayList<Vertex>();     // input array of vertices
      ArrayList<Vertex> outVerticesL = new ArrayList<Vertex>();   // vertices list after clipping against left edge
      ArrayList<Vertex> outVerticesT = new ArrayList<Vertex>();   // vertices list after clipping against top edge
      ArrayList<Vertex> outVerticesR = new ArrayList<Vertex>();   // vertices list after clipping against right edge
      ArrayList<Vertex> outVertices = new ArrayList<Vertex>();    // vertices list after clipping against bottom edge

      // Converting in array into list of vertices
      for(int i=0; i<in; i++) {
         inVertices.add(new Vertex(inx[i], iny[i]));
      }
      // Defining four edges of clip region
      Edge leftEdge = new Edge(llx,lly,llx,ury);
      Edge topEdge = new Edge(llx,ury,urx,ury);
      Edge rightEdge = new Edge(urx,ury,urx,lly);
      Edge bottomEdge = new Edge(urx,lly,llx,lly);
      // Clip against 4 edges
      SHPC(inVertices, outVerticesL, in, leftEdge);
      SHPC(outVerticesL, outVerticesT, outVerticesL.size(), topEdge);
      SHPC(outVerticesT, outVerticesR, outVerticesT.size(), rightEdge);
      SHPC(outVerticesR, outVertices, outVerticesR.size(), bottomEdge);
      // Convert out vertices list to array
      for(int i=0; i<outVertices.size(); i++) {
         outx[i] = outVertices.get(i).x;
         outy[i] = outVertices.get(i).y;
      }

      return outVertices.size();                                  // should return number of vertices in clipped poly.
   }

   public void SHPC(ArrayList<Vertex> inVertices, ArrayList<Vertex> outVertices,
         int inLength, Edge clipBoundary) {
      if(inVertices.size() > 0) {
      Vertex i;                                                   // intersection vertex
      Vertex p = new Vertex(inVertices.get(inLength-1).x, 
            inVertices.get(inLength-1).y);                        // (from vertex) last vertex is initial p
      Vertex s;                                                   // (to vertex) line goes from p to s
      
      for(int j=0; j<inLength; j++) {
         s = inVertices.get(j);
         if( inside(s, clipBoundary) ) {                          // case 1,4
            if( inside(p, clipBoundary) ) {                       // case 1
               outVertices.add(s);                                // add s to out vertices
            }else {                                               // case 4
               i = intersect(p, s, clipBoundary);
               outVertices.add(i);                                // add i to out vertices
               outVertices.add(s);                                // add s to out vertices
            }
         } else {                                                 // case 2,3
            if( inside(p, clipBoundary) ) {                       // case 2
               i = intersect(p, s, clipBoundary);
               outVertices.add(i);                                // add i to out vertices
            }                                                     // case 3 adds nothing
         }
         p = s;
      } // for 
   }
   }

   /**
    * Checks if vertex is inside or outside the clip boundary
    * @param   v              vertex to be checked
    * @param   clipBoundary   edge against which v is checked
    * @return                 true if v is inside the boundary
    */
   public boolean inside(Vertex v, Edge clipBoundary) {
      if( (clipBoundary.vertex2.x - clipBoundary.vertex1.x) * 
            (v.y - clipBoundary.vertex1.y) - 
            (clipBoundary.vertex2.y - clipBoundary.vertex1.y) * 
            (v.x - clipBoundary.vertex1.x) < 0 ) {
         return true;
      }else return false;
   }

   /**
    * Computes the intersection point between boundary and line formed
    * by vertices v1 and v2 using slope intercept form
    * @param   v1             first vertex
    * @param   v2             second vertex
    * @param   clipBoundary   edge
    * @return                 intersection vertex 
    */
   public Vertex intersect(Vertex v1, Vertex v2, Edge clipBoundary) {
      float intersectX = 0;                                       // x coordinate of intersection vertex
      float intersectY = 0;                                       // y coordinate of intersection vertex
      float m;                                                    // slope of line formed by v1 and v2
      float b;                                                    // intercept of line formed by v1 and v2
      float mBoundary;                                            // slope of clip boundary line
      float bBoundary;                                            // intercept of clip boundary line

      if(clipBoundary.vertex2.x == clipBoundary.vertex1.x) {      // if boundary is vertical
         intersectX = clipBoundary.vertex1.x;
         if(v2.x != v1.x) {                                       // line is not vertical
            m = (v2.y - v1.y) / (v2.x - v1.x);
            b = v1.y - (m * v1.x);
            intersectY = (m * intersectX) + b;
         }
      }else {
         mBoundary = (clipBoundary.vertex2.y - clipBoundary.vertex1.y)  // boundary is not vertical
            / (clipBoundary.vertex2.x - clipBoundary.vertex1.x);
         bBoundary = clipBoundary.vertex1.y - (mBoundary * clipBoundary.vertex1.x);
         if(v2.x == v1.x) {                                       // line is vertical
            intersectX = v1.x;
         }else {                                                  // line not vertical
            m = (v2.y - v1.y) / (v2.x - v1.x);
            b = v1.y - (m * v1.x);
            intersectX = (bBoundary - b) / (m - mBoundary);
         }

         intersectY = (mBoundary * intersectX) + bBoundary;
      }
      return new Vertex(intersectX, intersectY);                  // return intersection vertex
   }

}
