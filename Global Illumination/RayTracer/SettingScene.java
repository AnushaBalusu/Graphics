import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

public class SettingScene implements GLEventListener {

	private static GLU glu;
	private static int fps = 60;
	private static float widthHeightRatio;
	final static int slices = 40;
	final static int stacks = 40;

	// ********* FLOOR PARAMETERS ********* //
	static float floorWidth = 10;
	static float floorLength = 50;
	static float floorAngle = -85; // about X axis
	static float floorPos[] = { -2f, -2f, -50.0f };
	static float floorColor[] = {0.0f, 0.0f, 1.0f};

	// ********* SMALL SPHERE PARAMETERS ********* //
	final static float smallRadius = 1.2f;
	static float smallSpherePos[] = { -2f, 0.0f, -10.0f };
	static float smallSphereColor[] = { 0.4f, 0.4f, 0.4f };

	// ********* BIG SPHERE PARAMETERS ********* //
	final static float bigRadius = 1.4f;
	static float bigSpherePos[] = { 0.0f, 1.0f, -8.0f };
	static float bigSphereColor[] = { 0.3f, 0.3f, 0.3f };

	// ********* LIGHT PARAMETERS ********* //
	static float lightAmbient[] = { 0.2f, 0.2f, 0.2f, 1.0f };
	static float lightDiffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	static float lightSpecular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	static float lightPosition[] = { 0.0f, 5.0f,5.0f, 0.0f };
	static float mat_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	static float mat_shininess[] = { 100.0f };

	/**
	 * Set the position and orientation of camera
	 * @param gl
	 * @param glu
	 * @param distance
	 */
	private void setCamera(GL2 gl, GLU glu, float distance) {
		gl.glMatrixMode(GL2.GL_PROJECTION);	// Change to projection matrix.       
		gl.glLoadIdentity();
		glu.gluPerspective(45, widthHeightRatio, 1, 1000);	// Perspective.
		glu.gluLookAt(0, 0, 0, 0, 0, -1, 0, 1, 0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);	// Change back to model view matrix.
		gl.glLoadIdentity();
	}

	public static void drawFloor(GL2 gl, float[] floorPos, float[] color) {
		gl.glColor3f(color[0], color[1], color[2]);
		gl.glTranslatef(floorPos[0], floorPos[1],floorPos[2]);
		gl.glRotatef(floorAngle,1, 0, 0);

		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex3f(-floorWidth, -floorLength, 0);
		gl.glVertex3f(-floorWidth, +floorLength, 0);
		gl.glVertex3f(floorWidth, floorLength, 0);
		gl.glVertex3f(floorWidth, -floorLength, 0);
		gl.glEnd();
		gl.glLoadIdentity();

	}

	public static void drawSphere(GL2 gl, float[] pos, float radius,float[] color) {
		gl.glColor3f(color[0], color[1], color[2]);
		GLUquadric smallSphere = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(smallSphere, GLU.GLU_FILL);
		glu.gluQuadricNormals(smallSphere, GLU.GLU_FLAT);
		glu.gluQuadricOrientation(smallSphere, GLU.GLU_OUTSIDE);

		gl.glTranslatef(pos[0],pos[1],pos[2]);
		glu.gluSphere(smallSphere, radius, slices, stacks);
		glu.gluDeleteQuadric(smallSphere);
		gl.glLoadIdentity();
	}

	public static void setLight(GL2 gl) {
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular,0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess,0);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmbient, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDiffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightSpecular, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);
		gl.glLoadIdentity();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		// Clear screen.
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);

		setCamera(gl, glu, 100);
		setLight(gl);
		drawFloor(gl, floorPos, floorColor);
		drawSphere(gl, smallSpherePos, smallRadius, smallSphereColor);
		drawSphere(gl,bigSpherePos, bigRadius, bigSphereColor);
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = (GL2) drawable.getGL();
		glu = new GLU();
		gl.glShadeModel( GL2.GL_SMOOTH );
		gl.glClearColor( 0f, 0f, 0f, 0f );
		gl.glClearDepth( 1.0f );
		gl.glEnable( GL2.GL_DEPTH_TEST );
		gl.glDepthFunc( GL2.GL_LEQUAL );
		gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);
		widthHeightRatio = (float) width / (float) height;

	}

	public static void main(String[] args) {

		//getting the capabilities object of GL2 profile        
		final GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);

		// The canvas
		final GLCanvas glcanvas = new GLCanvas(capabilities);
		SettingScene l = new SettingScene();
		glcanvas.addGLEventListener(l);
		glcanvas.setSize(700, 500);

		//creating frame
		final JFrame frame = new JFrame ("Setting the Scene");

		//adding canvas to frame
		frame.getContentPane().add(glcanvas);

		frame.setSize(frame.getContentPane().getPreferredSize());
		frame.setVisible(true);
		// Start animator.
		final FPSAnimator animator = new FPSAnimator(glcanvas, fps);
		animator.start();
	}//end of main

}
