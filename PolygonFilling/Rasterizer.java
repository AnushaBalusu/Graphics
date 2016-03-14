//
//  Rasterizer.java
//
//  Created by Joe Geigel on 1/21/10.
//  Copyright 2010 Rochester Institute of Technology. All rights reserved.
//
//  Contributor:  Anusha Balusu
//

///
// 
// This is a class that performas rasterization algorithms
//
///

import java.util.*;

public class Rasterizer {

   ///
   // number of scanlines
   ///

   int n_scanlines;
   int activeEdges;
   NodeList al;   // active list
   NodeList[] el; // edge list
   simpleCanvas sC;  // canvas
   int yScanMax;  // Maximum scanLine to scan
   int index = 0; // current y value till which scanning is done
   ///
   // Constructor
   //
   // @param n - number of scanlines
   //
   ///

   Rasterizer (int n)
   {
      n_scanlines = n;
   }

   // Node in the bucketList of edge table for a scanLine/ node in activeList
   // Fill the edge table, each node contains ymin, ymax, x, m and link to next node
   class Node {
      int scanLine;
      int ymax;
      float x;
      float m;
      Node next;

      public Node() {

      }
      public Node(int ymin, int ymax, float x, float m) {
         this.scanLine = ymin;
         this.ymax = ymax;
         this.x = x; // x of ymin
         this.m = m; // inverse of slope
         this.next = null;
      }

      public Node(int ymin,int ymax, float x, float m, Node next) {
         this.scanLine = ymin;
         this.ymax = ymax;
         this.x = x; // x of ymin
         this.m = m; // inverse of slope
         this.next = next;
      }

   }
   // For active list or for bucket list related to each scan line in edge table
   class NodeList {      
      Node head;
      int scanLine;
      public NodeList() {
         this.scanLine = -1;
         head = new Node();
      }
      public NodeList(int scanLine) {
         this.scanLine = scanLine;
         head = new Node();
      }
      public void add(int s,Node node) {
         Node temp = head;
         while(temp.next != null) {  //add at the end
            temp = temp.next;
         }
         temp.next = node;
         node.next = null;
      }
      public Node remove() { // remove from front
         Node temp = head.next;
         head.next = head.next.next;
         temp.next = null;
         return temp;
      }
   }

   ///
   // Draw a filled polygon in the simpleCanvas C.
   //
   // The polygon has n distinct vertices. The 
   // coordinates of the vertices making up the polygon are stored in the 
   // x and y arrays.  The ith vertex will have coordinate  (x[i], y[i])
   //
   // You are to add the implementation here using only calls
   // to C.setPixel()
   ///
   // array of polygon vertices are given in the order to be drawn
   public void drawPolygon(int n, int x[], int y[], simpleCanvas C)
   {
      // YOUR IMPLEMENTATION GOES HERE
      yScanMax = 0;
      index = 0;
      activeEdges = 0;
      sC = C;
      el = new NodeList[n];
      al = new NodeList(-1);
      int scan = initializeET(n,x,y);
      sortET(scan);
      fill(scan);

   }

   // For filling - remove edges from AL, add new edges to AL, sort, fill pixel
   public void fill(int scan) {
      Node n;
      int y=0;
      for(int i=0; i<=yScanMax; i++) {
         removeEdges(y);
         addEdges(y,scan);
         if(activeEdges > 0) {
            sortEdges();
            fillPixels(y);
            updateX();
         }
         y++;
      }
   }

   // Updating x values as x + 1/m
   public void updateX() {
      Node temp = al.head.next;
      while(temp != null) {
         temp.x += temp.m;
         temp = temp.next;
      }
   }

   // From x1 to x2, turn pixels on
   public void fillPixels(int y) {
      Node temp = al.head.next;
      int x1;
      int x2;
      while(temp != null) {
         x1 = (int) (temp.x + 0.5);
         temp = temp.next;
         x2 = (int) (temp.x - 0.5);
         temp = temp.next;
         for(int x = x1; x<x2; x++) {
            sC.setPixel(x,y);
         }
      }
   }

   // sort edges on x; within groups of same x, sort on 1/m
   public void sortEdges() {
      NodeList alTemp = new NodeList();
      Node temp;
      Node t;
      int j;
      for(int i=0; i<activeEdges; i++) {
         temp = al.remove();         

         if(alTemp.head.next == null) {
            alTemp.add(-1,temp);
         }else {
            t = alTemp.head;         
            while(t.next!= null && t.next.x < temp.x) {
               t = t.next;                        
            }
            if(t.next != null && t.next.x == temp.x) { // for similar x, check 1/m values
               while(t.next!= null && t.next.m < temp.m) { // check 1/m
                  t = t.next;
               }
               temp.next = t.next;
               t.next = temp;
               //
            }else if(t.next != null && t.next.x > temp.x) {
               temp.next = t.next;
               t.next = temp;
            }else if(t.next == null) {
               t.next = temp;
            }
         }
      }
      al = alTemp;
   }

   // add edges to Active list
   public void addEdges(int y,int scan) {
      if(index < scan) 
         for(int i=index;i<=yScanMax; i++) {
            if(el[index].scanLine == y) {
               Node temp = el[index].head;
               while(temp.next!=null) {
                  Node t1 = new Node(temp.next.scanLine,temp.next.ymax,temp.next.x,temp.next.m);           
                  if(al == null) {
                  }
                  al.add(-1,t1);
                  activeEdges++;
                  temp = temp.next;
               }
               index++;
            }
            break;
         }
   }

   // Remove edges from active list
   public void removeEdges(int y) {
      int i=0;
      if(al != null) {
         Node temp = al.head;
         while(temp.next!=null) {
            if(temp.next.ymax == y) {
               temp.next = temp.next.next;
               activeEdges--;
            }else {
               temp = temp.next;
            }
         }
      }
   }

   // After forming the edge list, sort based on scanline (ymin) values
   public void sortET(int scan) {
      NodeList temp;
      int j;
      for(int i=1; i<scan; i++) {
         temp = el[i];
         for(j=i-1; j>=0; j--) {
            if(temp.scanLine < el[j].scanLine ) {
               el[j+1] = el[j];
            }else {
               break;
            }            
         }
         el[j+1] = temp;
      }
   }

   // Read the coordinates and fill the edge table
   public int initializeET(int n,int x[], int y[]) {
      int temp;
      float x1;
      int y1;
      float x2;
      int y2;
      boolean added = false;
      float m=0;
      int scan=0;
      int nexti=0;
      yScanMax = 0;
      for(int i=0; i<n; i++) {
         nexti = (i+1)%n;
         x1 = (float) x[i];
         y1 = y[i];
         x2 = (float) x[nexti];
         y2 = y[nexti];
         if(y[i] > y[nexti]) {  // store ymin in y[i]
            y1 = y[nexti];
            x1 = (float) x[nexti];

            y2 = y[i];
            x2 = (float) x[i];
         }
         yScanMax = (yScanMax > y[i]) ? yScanMax : y[i];
         m = (float) (x2-x1) / (y2-y1);
         if((y2-y1) !=0) {
            Node node = new Node(y1,y2, x1, m);
            added = false;
            for(int j=0; j<scan; j++) {
               if(el[j].scanLine == y1) {
                  el[j].add(y1,node);
                  added = true;
                  break;
               }
            }
            if(added==false) {
               el[scan] = new NodeList(y1);
               el[scan].add(y[i],node);
               scan++;
            }
         }
      }

      return scan;
   }
}
