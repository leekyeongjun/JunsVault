```python
# coding: utf-8
from OpenGL.GL import *
from OpenGL.GLU import *
import glfw
import sys

######################################
# Camera
######################################
# 카메라 회전을 위한 새로운 변수 추가
camera_yaw = 0
camera_pitch = 0

# 키 입력에 따른 카메라 회전 처리를 위한 콜백 함수
def key_callback(window, key, scancode, action, mods):
    global camera_yaw, camera_pitch
    if action == glfw.PRESS or action == glfw.REPEAT:
        if key == glfw.KEY_1:
            camera_yaw += 10 
        elif key == glfw.KEY_2:
            camera_pitch -= 10  
        elif key == glfw.KEY_3:
            camera_yaw -= 10  
        elif key == glfw.KEY_W:
            camera_pitch += 10  

######################################
# OpenGL funcs
######################################
def initialize(window):
    glClearColor(0.0, 0.0, 0.0, 0.0)
    glClearDepth(1.0)
    glDepthFunc(GL_LESS)
    glEnable(GL_DEPTH_TEST)
    glMatrixMode(GL_PROJECTION);            # Select The Projection Matrix
    glLoadIdentity();                       # Reset The Projection Matrix
    aspect=1
    gluPerspective(45, aspect, 1, 1024);

def draw():
    global camera_yaw, camera_pitch
    # clear
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

    # view
    glMatrixMode(GL_MODELVIEW)
    glLoadIdentity()
    glTranslatef(0.0, 0.0, -2.0)
    glRotatef(camera_yaw, 0, 1, 0)
    glRotatef(camera_pitch, 1, 0, 0)
    # pyramid
    draw_pyramid()
    glFlush()

######################################
# Shader
######################################
# Checks for GL posted errors after appropriate calls
def printOpenGLError():
    err = glGetError()
    if (err != GL_NO_ERROR):
        print('GLERROR: ', gluErrorString(err))
        #sys.exit()
class Shader(object):
    def initShader(self, vertex_shader_source, fragment_shader_source):
        # create program
        self.program=glCreateProgram()
        print('create program')
        printOpenGLError()
        # vertex shader
        print('compile vertex shader...')
        self.vs = glCreateShader(GL_VERTEX_SHADER)
        glShaderSource(self.vs, [vertex_shader_source])
        glCompileShader(self.vs)
        glAttachShader(self.program, self.vs)
        printOpenGLError()
        # fragment shader
        print('compile fragment shader...')
        self.fs = glCreateShader(GL_FRAGMENT_SHADER)
        glShaderSource(self.fs, [fragment_shader_source])
        glCompileShader(self.fs)
        glAttachShader(self.program, self.fs)
        printOpenGLError()
        print('link...')
        glLinkProgram(self.program)
        printOpenGLError()
    def begin(self):
        if glUseProgram(self.program):
            printOpenGLError()
    def end(self):
        glUseProgram(0)
####################################
# vertices
####################################
s= 0.5
vertices=[
        0, s, 0,
        -s, -s, s,
        s, -s, s,
        s, -s, -s,
        -s, -s, -s,
        ]
colors=[
        1, 0, 0,
        0, 1, 0,
        0, 0, 1,
        0, 1, 1,
        1, 0, 1,
        ]
indices=[
        0, 1, 2,
        0, 2, 3,
        0, 3, 4,
        0, 4, 1,
        1, 2, 3,
        1, 3, 4,
        ]

def draw_pyramid():
    glEnableClientState(GL_VERTEX_ARRAY);
    glEnableClientState(GL_COLOR_ARRAY);
    glVertexPointer(3, GL_FLOAT, 0, vertices);
    glColorPointer(3, GL_FLOAT, 0, colors)
    glDrawElements(GL_TRIANGLES, len(indices), GL_UNSIGNED_INT, indices);
    glDisableClientState(GL_COLOR_ARRAY)
    glDisableClientState(GL_VERTEX_ARRAY);
buffers=None

def create_vbo():
    buffers = glGenBuffers(3)
    glBindBuffer(GL_ARRAY_BUFFER, buffers[0])
    glBufferData(GL_ARRAY_BUFFER, 
            len(vertices)*4,  # byte size
            (ctypes.c_float*len(vertices))(*vertices), 
            GL_STATIC_DRAW)
    glBindBuffer(GL_ARRAY_BUFFER, buffers[1])
    glBufferData(GL_ARRAY_BUFFER, 
            len(colors)*4, # byte size 
            (ctypes.c_float*len(colors))(*colors), 
            GL_STATIC_DRAW)
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[2])
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, 
            len(indices)*4, # byte size
            (ctypes.c_uint*len(indices))(*indices), 
            GL_STATIC_DRAW)
    return buffers

def draw_vbo():
    glEnableClientState(GL_VERTEX_ARRAY);
    glEnableClientState(GL_COLOR_ARRAY);
    glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
    glVertexPointer(3, GL_FLOAT, 0, None);
    glBindBuffer(GL_ARRAY_BUFFER, buffers[1]);
    glColorPointer(3, GL_FLOAT, 0, None);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[2]);
    glDrawElements(GL_TRIANGLES, len(indices), GL_UNSIGNED_INT, None);
    glDisableClientState(GL_COLOR_ARRAY)
    glDisableClientState(GL_VERTEX_ARRAY);

def draw_cube2():
    global buffers
    if buffers==None:
        buffers=create_vbo()
    draw_vbo()

shader=None
def draw_cube3():
    global shader, buffers
    if shader==None:
        shader=Shader()
        shader.initShader('''
void main()
{
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    gl_FrontColor = gl_Color;
}
        ''',
        '''
void main()
{
    gl_FragColor = gl_Color;
}
        ''')
        buffers=create_vbo()

    shader.begin()
    draw_vbo()
    shader.end()

##############################################################################


if __name__=="__main__":

    if not glfw.init():
        print ('GLFW initialization failed')
        sys.exit(-1)

    window = glfw.create_window(480,480, '2019092824-5-1', None, None)
    if not window:
        glfw.terminate()
        sys.exit(-1)

    glfw.make_context_current(window)
    glfw.set_key_callback(window, key_callback)
    glfw.swap_interval(1)


    initialize(window)
    while not glfw.window_should_close(window):
        glfw.poll_events()
        draw()
        glfw.swap_buffers(window)

    glfw.terminate()


```
