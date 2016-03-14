#ifdef __APPLE__
#include <OpenGL/gl.h>
#else
#include <GL/gl.h>
#endif
#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

//#include<GL/glut.h>
//#include<GL/gl.h>
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<math.h>

void init(void)
{
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluOrtho2D(0,100,0,100);
	glClearColor(1.0,1.0,1.0,0);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
}
float x[4] = {20,40,20,60};
float y[4] = {20,50,40,35};
//x[0]=20,y[0]=20,x[1]=40,y[1]=50,x[2]=20,y[2]=40,x[3]=20,y[3]=75;
int angle[4] = {0,0,0,0};
int color[4] = {0,1,2,3};
int i,fish_no=0;
float q[4]={80,80,80,80};
float p[4] = {40,50,60,70};//{rand()%78+1,rand()%78+1,rand()%78+1,rand()%78+1};
int score[4]={0};
int timer = 0,t=50;
char buffer[100];
int gameState = 0;
int flag = 0;
float inc = 1.0;
int level=0,count=0;
int sum=0;

void keyboard( int key,int x1,int y1 )
{
	if( key == 27 )  exit(0);
	if(gameState == 1 || timer>=t);
	else {	
		if( key == GLUT_KEY_F1 )
		{	
			fish_no = 0;	
		}
		if( key == GLUT_KEY_F2 )
		{
			fish_no = 1;
		}	
		if( key == GLUT_KEY_F3 )
		{
			fish_no = 2;
		}	
		if( key == GLUT_KEY_F4 )
		{
			fish_no = 3;
		}
		//
		i = fish_no;
		//	printf("%d\n",i);
		if( key == GLUT_KEY_RIGHT)
		{
			if(x[i]>2 && x[i]<96) x[i]++;
			angle[i] = 0 ;
		}
		else if( key == GLUT_KEY_LEFT)
		{
			if(x[i]>3 && x[i]<97) x[i]--;
			angle[i] = 180 ;
		}
		else if( key == GLUT_KEY_UP)
		{
			if(y[i]>1 && y[i]<68) y[i]++;
		}
		else if( key == GLUT_KEY_DOWN)
		{
			if(y[i]>2 && y[i]<69) y[i]--;
		}
	}
}

void clock( int z )
{
	if(gameState==1 || gameState ==2 )
		timer=timer;
	else if(gameState==0){

		if(timer<=t)
		{
			timer = timer + 1;
		}
	}
	glutTimerFunc(1000,clock,0);
}
void print_string(void* font, char* s)
{
	if (s && strlen(s)) {
		while (*s) {
			glutBitmapCharacter(font, *s);
			s++;
		}
	}
}

void fish(float X,float Y,int j)	
{
	float x,y,z;
	int angl,a=4,b=2;
	glPushMatrix();
	glTranslatef(X,Y,0);
	glRotatef(angle[j],0,1,0);
	//		glColor3f(1,0,0);		 
	glBegin(GL_TRIANGLE_FAN);	//ellipse
	glVertex2f(1,0);
	for(angl=40;angl<=320;angl++)
	{
		x=a*(cos(angl*3.14/180));
		y=b*(sin(angl*3.14/180));
		glVertex2f(x,y);
	}
	glEnd();
	//	glColor3f(1,0,0);
	glBegin(GL_TRIANGLES);	//tail
	glVertex3f(-4.0,0.0,0.0);
	glVertex3f(-6.0,2.0,0.0);
	glVertex3f(-6.0,-2.0,0.0);
	glEnd();
	//	glColor3f(1,0,0);
	glBegin(GL_TRIANGLES);	//fin	
	glVertex2f(0.8,2.0);
	glVertex2f(-1.0,2.0);
	glVertex2f(-1.0,3.0);
	glEnd();
	//	glColor3f(1,0,0);
	glBegin(GL_TRIANGLES);	//fin
	glVertex3f(0.8,-2.0,0.0);
	glVertex3f(-1.0,-2.0,0.0);
	glVertex3f(-1.0,-3.0,0.0);
	glEnd();
	glPointSize(3);
	glBegin(GL_POINTS);	//eye
	glColor3f(0,0,1);
	glVertex2f(1.0,1.0);
	glEnd();
	glPopMatrix();
	glColor4f(0.0, 0.0, 0.0, 0.0);
	glRasterPos2f(X-1, Y-1);
	sprintf(buffer,"%d",j+1);
	print_string(GLUT_BITMAP_8_BY_13,buffer);
}


void smallfish(float X,float Y,int j)	
{
	float x,y,z;
	int angl;
	float a=2.5,b=1.5;
	glPushMatrix();
	glTranslatef(X,Y,0);
	//	glRotatef(angle[j],0,1,0);
	//		glColor3f(1,0,0);		 
	glBegin(GL_TRIANGLE_FAN);	//ellipse
	glVertex2f(1,0);
	for(angl=30;angl<=330;angl++)
	{
		x=a*(cos(angl*3.14/180));
		y=b*(sin(angl*3.14/180));
		glVertex2f(x,y);
	}
	glEnd();
	//	glColor3f(1,0,0);
	glBegin(GL_TRIANGLES);	//tail
	glVertex3f(-1.5,0.0,0.0);
	glVertex3f(-4.0,1.0,0.0);
	glVertex3f(-4.0,-1.0,0.0);
	glEnd();
	//	glColor3f(1,0,0);
	glBegin(GL_TRIANGLES);	//fin	
	glVertex2f(0.8,1.0);
	glVertex2f(-1.0,1.0);
	glVertex2f(-1.0,2.0);
	glEnd();
	//	glColor3f(1,0,0);
	glBegin(GL_TRIANGLES);	//fin
	glVertex2f(0.8,-1.0);
	glVertex2f(-1.0,-1.0);
	glVertex2f(-1.0,-2.0);
	glEnd();
	glPointSize(2);
	glBegin(GL_POINTS);	//eye
	glColor3f(0,0,1);
	glVertex2f(1.0,1.0);
	glEnd();
	glPopMatrix();
	glColor4f(1.0, 1.0, 1.0, 0.0);
	glRasterPos2f(X-1, Y-1);
	sprintf(buffer,"%d",j+1);
	print_string(GLUT_BITMAP_8_BY_13,buffer);
}

void mykey(unsigned char key,int x,int y)
{
	if(key == 27) exit(0);
	if(key==32 && timer <= t )
		if(gameState==0)
			gameState=1;
		else if(gameState==1)
			gameState=0;
	if(key==104 && timer <= t )
		if(gameState==0)
			gameState=2;
		else if(gameState==2)
			gameState=0;
}

void sort()
{
	int j,k,temp,s[4],c[4]={0,1,2,3};
	for(j=0;j<4;j++)
		s[j] = score[j];
	for(j=0;j<4;j++)
	{
		for(k=0;k<3;k++)
		{
			if(s[k]>s[k+1])
			{
				temp = c[k];
				c[k] = c[k+1];
				c[k+1] = temp;
				temp = s[k];
				s[k] = s[k+1];
				s[k+1] = temp;
			}
		}
	}
	for(j=0;j<4;j++)
		color[j] = c[j];

}

void check(int i)
{
	int j,k,temp;
	for(j=0;j<4;j++){
		if( (((p[j] >=(x[i]+1))&&(p[j] <=(x[i]+4))) && ((q[j] >=(y[i]-2*sin(40*3.14/180)))&&(q[j] <=(y[i]+2*sin(40*3.14/180)))) && angle[i] == 0) || (((p[j] >(x[i]-4))&&(p[j] <=(x[i]-1))) && ((q[j] >=(y[i]-2*sin(40*3.14/180)))&&(q[j] <=(y[i]+2*sin(40*3.14/180))) && angle[i] == 180)))
		{
			flag = 1;
			score[i]=score[i]+5;     //after certain score change colr to noe dat it ate some no. of bread
			count++;
			sort();
			q[j]=78;p[j]=rand()%78+1;
		}
		else if( ((( p[j] >=(x[i]-6)) && (p[j] <=(x[i]+1))) && ((q[j] >=(y[i]-2*sin(40*3.14/180)))&&(q[j] <=(y[i]+2*sin(40*3.14/180)))) && angle[i] == 0) ||  ((( p[j] >=(x[i]-1)) && (p[j] <=(x[i]+6))) &&  ((q[j] >=(y[i]-2*sin(40*3.14/180)))&&(q[j] <=(y[i]+2*sin(40*3.14/180)))) && angle[i] == 180))  // dont inc
		{q[j]=80;p[j]=rand()%78+1;}
	}
}
void draw()
{
	glClear(GL_COLOR_BUFFER_BIT);
	glLoadIdentity();
	glColor3f(0.2,0.3,1.0);
	glBegin(GL_POLYGON);		//tank
	glVertex2f(0.0,0.0);
	glVertex2f(100.0,0.0);
	glColor3f(30,100,255);
	glVertex2f(100.0,70.0);
	glVertex2f(0.0,70.0);
	glEnd();
	glColor3f(0.9,0.9,0.9);
	glBegin(GL_POLYGON);		//border of scoreboard
	glVertex2f(0.0,100.0);
	glVertex2f(100.0,100.0);
	glColor3f(30,144,255);
	glVertex2f(100.0,70.0);
	glVertex2f(0.0,70.0);
	glEnd();
	glColor3f(0.9,0.9,1);
	glLineWidth(1);                //Line
	glBegin(GL_LINES);
	glVertex2f(0.0,69.0);
	glVertex2f(100.0,69.0);
	glEnd();
	glColor3f(0.5,0.5,0.5);        // drawing small fishes
	smallfish(10.0,95.0,0);
	glColor3f(0.5,0.5,0.5);        // drawing small fishes
	smallfish(10.0,90.0,1);
	glColor3f(0.5,0.5,0.5);        // drawing small fishes
	smallfish(10.0,85.0,2);
	glColor3f(0.5,0.5,0.5);        // drawing small fishes
	smallfish(10.0,80.0,3);

	int j;
	sum = score[0]+score[1]+score[2]+score[3];
	if( (timer <= t) && gameState == 0 )
	{
	glColor3f(1,0.5,1);        // drawing fishes
	fish(x[color[0]],y[color[0]],color[0]);
	glColor3f(0,1,1);
	fish(x[color[1]],y[color[1]],color[1]);
	glColor3f(0.5,1,0.5);
	fish(x[color[2]],y[color[2]],color[2]);
	glColor3f(1,1,0);
	fish(x[color[3]],y[color[3]],color[3]);
	glColor3f(0,0,0);
	glPointSize(5);	
	glBegin(GL_POINTS);
	glVertex2f(p[0],q[0]);
	glVertex2f(p[1],q[1]);
	glVertex2f(p[2],q[2]);
	glVertex2f(p[3],q[3]);
	glEnd();

		check(fish_no); //check if food touches fish and vanish if it does
		if(flag == 1){
			inc-=0.009;
			if(inc>=0){
				glColor3f(0.0,0.0,0.0);
				glRasterPos2f(x[i],y[i]+4);
				sprintf(buffer,"+5");
				print_string(GLUT_BITMAP_HELVETICA_12,buffer);
			}
			else{
				inc = 1;
				flag=0;}
		}
		for(j=0;j<4;j++){
			q[j]=q[j]-0.009;	//speed
			if(count == 5 && level == 0) {
				level++;			
				q[j]=q[j]-0.015;
				count=0;
//				timer=t;
	/*	glRasterPos2f(40, 40);
		sprintf(buffer,"Level %d",level );
		print_string(GLUT_BITMAP_HELVETICA_18,buffer);
		sprintf(buffer,"\n" );
		print_string(GLUT_BITMAP_HELVETICA_18,buffer);
		glRasterPos2f(40, 30);
		sprintf(buffer,"Your Score is %d",sum );
		print_string(GLUT_BITMAP_HELVETICA_18,buffer);*/
			}
			if(count == 15 && level == 1) {
				level++;			
				q[j]=q[j]-0.025;
				count=0;
		//		timer=t;
			}
			if(count == 30 && level == 2) {
				level++;			
				q[j]=q[j]-0.030;
				count=0;
		//		timer=t;
			}
			else if(level==1)
			{
				q[j]=q[j]-0.015;
			}
			else if(level==2)
			{
				q[j]=q[j]-0.025;
			}
			
			if(q[j]<=0)
			{	
				q[j]=80;
				p[j]=rand()%78+1; 
			}
		}
	//	checkLevel();

	}
	else if(gameState == 1 ){
	glColor3f(1,0.5,1);        // drawing fishes
	fish(x[color[0]],y[color[0]],color[0]);
	glColor3f(0,1,1);
	fish(x[color[1]],y[color[1]],color[1]);
	glColor3f(0.5,1,0.5);
	fish(x[color[2]],y[color[2]],color[2]);
	glColor3f(1,1,0);
	fish(x[color[3]],y[color[3]],color[3]);
	glColor3f(0,0,0);
	glPointSize(5);	
	glBegin(GL_POINTS);
	glVertex2f(p[0],q[0]);
	glVertex2f(p[1],q[1]);
	glVertex2f(p[2],q[2]);
	glVertex2f(p[3],q[3]);
	glEnd();

		glRasterPos2f(40, 40);
		sprintf(buffer,"Paused !!");
		print_string(GLUT_BITMAP_HELVETICA_18,buffer);
		
		}
	else if(gameState == 2 )
	{
		glColor3f(0.0,0.0,0.0);
		glRasterPos2f(30, 60);
		sprintf(buffer,"Time of play : %d seconds",t);
		print_string(GLUT_BITMAP_HELVETICA_18,buffer);
		sprintf(buffer,"\n" );
		print_string(GLUT_BITMAP_HELVETICA_18,buffer);
		glRasterPos2f(10, 55);
		sprintf(buffer,"Use function keys to make the corresponding fish active");
		print_string(GLUT_BITMAP_HELVETICA_18,buffer);
		sprintf(buffer,"\n" );
		print_string(GLUT_BITMAP_HELVETICA_18,buffer);
		glRasterPos2f(20, 50);
		sprintf(buffer,"Use arrow keys to move the active fish");
		print_string(GLUT_BITMAP_HELVETICA_18,buffer);
		sprintf(buffer,"\n" );
		print_string(GLUT_BITMAP_HELVETICA_18,buffer);
		glRasterPos2f(20, 45);
		sprintf(buffer,"Fishes change color as per their scores");
		print_string(GLUT_BITMAP_HELVETICA_18,buffer);
	glColor3f(1.0,1.0,0.0);        // drawing small fishes
	smallfish(40.0,40.0,0);
	glColor3f(0.5,1.0,0.5);        // drawing small fishes
	smallfish(40.0,35.0,1);
	glColor3f(0.0,1.0,1.0);        // drawing small fishes
	smallfish(40.0,30.0,2);
	glColor3f(1.0,0.5,1.0);        // drawing small fishes
	smallfish(40.0,25.0,3);
		//		glutSwapBuffers();
		glColor3f(0.0,0.0,0.0);
		glRasterPos2f(20, 15);
		sprintf(buffer,"Speed of food chunks increases at each level");
		print_string(GLUT_BITMAP_HELVETICA_18,buffer);
		sprintf(buffer,"\n" );
		print_string(GLUT_BITMAP_HELVETICA_18,buffer);
	}
	else  {
	glColor3f(1,0.5,1);        // drawing fishes
	fish(x[color[0]],y[color[0]],color[0]);
	glColor3f(0,1,1);
	fish(x[color[1]],y[color[1]],color[1]);
	glColor3f(0.5,1,0.5);
	fish(x[color[2]],y[color[2]],color[2]);
	glColor3f(1,1,0);
	fish(x[color[3]],y[color[3]],color[3]);
	glColor3f(0,0,0);
	glPointSize(5);	
	glBegin(GL_POINTS);
	glVertex2f(p[0],q[0]);
	glVertex2f(p[1],q[1]);
	glVertex2f(p[2],q[2]);
	glVertex2f(p[3],q[3]);
	glEnd();

		glRasterPos2f(40, 40);
		sprintf(buffer,"TIME OVER !!" );
		print_string(GLUT_BITMAP_HELVETICA_18,buffer);
		sprintf(buffer,"\n" );
		print_string(GLUT_BITMAP_HELVETICA_12,buffer);
		glRasterPos2f(40, 35);
		sprintf(buffer,"Levels played %d",level );
		print_string(GLUT_BITMAP_HELVETICA_18,buffer);
		sprintf(buffer,"\n" );
		print_string(GLUT_BITMAP_HELVETICA_12,buffer);
		glRasterPos2f(40, 30);
		sprintf(buffer,"Your Score is %d",sum );
		print_string(GLUT_BITMAP_HELVETICA_18,buffer);
		//	glutSwapBuffers();
	}
	
	glColor4f(0.0, 0.0, 0.0, 0.0);
	glRasterPos2f(20, 94);
	sprintf(buffer,"--->      %d      ",score[0]);
	print_string(GLUT_BITMAP_HELVETICA_12,buffer);
	glRasterPos2f(20, 89);
	sprintf(buffer,"--->      %d      ",score[1]);
	print_string(GLUT_BITMAP_HELVETICA_12,buffer);
	glRasterPos2f(20, 84);
	sprintf(buffer,"--->      %d      ",score[2]);
	print_string(GLUT_BITMAP_HELVETICA_12,buffer);
	glRasterPos2f(20, 79);
	sprintf(buffer,"--->      %d      ",score[3]);
	print_string(GLUT_BITMAP_HELVETICA_12,buffer);
	//	glutSwapBuffers();
	glRasterPos2f(80,95);
	sprintf(buffer,"Time Left:%d",t-timer+1);
	print_string(GLUT_BITMAP_9_BY_15,buffer);
	glRasterPos2f(80,90);
	sprintf(buffer,"Help -> h");
	print_string(GLUT_BITMAP_HELVETICA_12,buffer);
	//	print_string(GLUT_BITMAP_9_BY_15,buffer);
	glRasterPos2f(80,87);
	sprintf(buffer,"Pause -> Spacebar");
	print_string(GLUT_BITMAP_HELVETICA_12,buffer);
	glRasterPos2f(80,84);
	sprintf(buffer,"Quit -> Esc");
	print_string(GLUT_BITMAP_HELVETICA_12,buffer);
	glRasterPos2f(50,85);
	sprintf(buffer,"Level  %d",level);
	print_string(GLUT_BITMAP_HELVETICA_18,buffer);
	//	print_string(GLUT_BITMAP_9_BY_15,buffer);
	glutSwapBuffers();
}


int main(int argc,char **argv)
{
	glutInit(&argc,argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE);
	//glutInitWindowPosition(100,100);
	glutInitWindowSize(600,600);
	glutCreateWindow("Fish and Food");
	glutDisplayFunc(draw);
	glutIdleFunc(draw);
	glutTimerFunc(1000,clock,0);
	glutSpecialFunc( keyboard );
	glutKeyboardFunc(mykey);
	init();
	glutMainLoop();
}
