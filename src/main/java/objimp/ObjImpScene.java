package objimp;

import com.obj.*;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

//import processing.core.PImage;
//import processing.core.PVector;


public class ObjImpScene {
    int _callListID;
    boolean _callListCompiled;

    ArrayList<float[]> _vertexList;
    ArrayList<float[]> _normalList;
    ArrayList<float[]> _texcoordList;
    ArrayList<ObjImpMesh> _meshList;

    private WavefrontObject _obj;

    public ObjImpScene() {
        _callListID = 0;
        _callListCompiled = false;

        _callListID = GL11.glGenLists(1);

        _vertexList = new ArrayList<float[]>();
        _normalList = new ArrayList<float[]>();
        _texcoordList = new ArrayList<float[]>();

        _meshList = new ArrayList<ObjImpMesh>();

        _obj = null;
    }

    public void load(String name) {
        load(name, 1, 1, 1);
    }

    public void load(String name, float s) {
        load(name, s, s, s);
    }

    public void load(String name, float sx, float sy, float sz) {
        _obj = new WavefrontObject(name, sx, sy, sz);

        ArrayList<Group> groups = _obj.getGroups();
        for (int gi = 0; gi < groups.size(); gi++) {
            Group g = groups.get(gi);
            Material gm = g.getMaterial();

            ObjImpMesh mesh = new ObjImpMesh();
            mesh.setName(g.getName());

            if (gm != null) {
                if (gm.getKa() != null)
                    mesh._material._ambient = new float[]{gm.getKa().getX(), gm.getKa().getY(), gm.getKa().getZ()};
                else mesh._material._diffuse = new float[]{0.0f, 0.0f, 0.0f};
                if (gm.getKd() != null)
                    mesh._material._diffuse = new float[]{gm.getKd().getX(), gm.getKd().getY(), gm.getKd().getZ()};
                else mesh._material._diffuse = new float[]{0.5f, 0.5f, 0.5f};
                if (gm.getKs() != null)
                    mesh._material._specular = new float[]{gm.getKs().getX(), gm.getKs().getY(), gm.getKs().getZ()};
                else mesh._material._specular = new float[]{0, 0, 0};
                mesh._material._shininess = gm.getShininess();

                if (gm.texName != null && gm.texName.length() > 0) {
                    try {
                        org.newdawn.slick.opengl.Texture _tex = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(name.replaceFirst("[^/]+$", "") + gm.texName));
                        mesh._material._texId = _tex.getTextureID();
                        //System.out.println( "mesh._material._texId: " + mesh._material._texId  );
                    } catch (IOException e) {
                        System.err.println("(OBJScene) Failed loading texture '" + gm.texName + "' with error: " + e);
                    }
                }
            } else {
                // default material
                mesh._material._ambient = new float[]{0, 0, 0, 0};
                mesh._material._diffuse = new float[]{0.5f, 0.5f, 0.5f, 1};
                mesh._material._specular = new float[]{1, 1, 1, 1};
                mesh._material._shininess = 64.0f;
                mesh._material._texId = 0;
            }


            for (int fi = 0; fi < g.getFaces().size(); fi++) {
                Face f = g.getFaces().get(fi);
                int[] idx = f.vertIndices;
                int[] nidx = f.normIndices;
                int[] tidx = f.texIndices;


                if (f.getType() == Face.GL_TRIANGLES) {
                    ObjImpFace face = new ObjImpFace();
                    face._a = idx[0];
                    face._b = idx[1];
                    face._c = idx[2];
                    face._na = nidx[0];
                    face._nb = nidx[1];
                    face._nc = nidx[2];
                    face._ta = tidx[0];
                    face._tb = tidx[1];
                    face._tc = tidx[2];
                    mesh.addFace(face);
                } else if (f.getType() == Face.GL_QUADS) {
                    ObjImpFace face = new ObjImpFace();
                    ObjImpFace face2 = new ObjImpFace();

                    face._a = idx[0];
                    face._b = idx[1];
                    face._c = idx[2];
                    face._na = nidx[0];
                    face._nb = nidx[1];
                    face._nc = nidx[2];
                    face._ta = tidx[0];
                    face._tb = tidx[1];
                    face._tc = tidx[2];

                    face2._a = idx[0];
                    face2._b = idx[2];
                    face2._c = idx[3];
                    face2._na = nidx[0];
                    face2._nb = nidx[2];
                    face2._nc = nidx[3];
                    face2._ta = tidx[0];
                    face2._tb = tidx[2];
                    face2._tc = tidx[3];

                    mesh.addFace(face);
                    mesh.addFace(face2);
                }
            }

            for (int vi = 0; vi < _obj.getVertices().size(); vi++) {
                Vertex v = (Vertex) _obj.getVertices().get(vi);
                _vertexList.add(new float[]{v.getX(), v.getY(), v.getZ()});
            }

            for (int vi = 0; vi < _obj.getNormals().size(); vi++) {
                Vertex v = (Vertex) _obj.getNormals().get(vi);
                _normalList.add(new float[]{v.getX(), v.getY(), v.getZ()});
            }

            for (int vi = 0; vi < _obj.getTextures().size(); vi++) {
                TextureCoordinate tc = (TextureCoordinate) _obj.getTextures().get(vi);
                _texcoordList.add(new float[]{tc.getU(), tc.getV(), tc.getW()});
            }

	/*      for( int vi=0; vi<g.indices.size(); vi++ )
          {
	        int tc = (int)g.indices.get( vi );
	        mesh.addVertex( new Vector3( tc.getX(), tc.getY(), tc.getZ()) );
	      }

	      for( int vi=0; vi<g.vertices.size(); vi++ )
	      {
	        Vertex tc = (Vertex)g.vertices.get( vi );
	        mesh.addVertex( new Vector3( tc.getX(), tc.getY(), tc.getZ()) );
	      }

	      for( int vi=0; vi<g.normals.size(); vi++ )
	      {
	        Vertex tc = (Vertex)g.normals.get( vi );
	        mesh.addNormal( new Vector3( tc.getX(), tc.getY(), tc.getZ()) );
	      }

	      for( int vi=0; vi<g.texcoords.size(); vi++ )
	      {
	        TextureCoordinate tc = (TextureCoordinate)g.texcoords.get( vi );
	        mesh.addTexCoord( new Vector3( tc.getU(), tc.getV(), tc.getW()) );
	      }*/

            // Finally add mesh to scene
            addMesh(mesh);
        }
    }

    public void addMesh(ObjImpMesh mesh) {
        _meshList.add(mesh);
    }


    public void draw() {
        // If the list is compiled and everything is ok, render
        if (_callListID > 0 && _callListCompiled) {
            GL11.glCallList(_callListID);
            return;
        }

        if (_callListID > 0 && !_callListCompiled) {
            //_callListID = GL11.glGenLists( 1 );
            GL11.glNewList(_callListID, GL11.GL_COMPILE);
        }


        // Render all scene
        for (int i = 0; i < _meshList.size(); i++) {
            ObjImpMesh m = _meshList.get(i);

            // If current material has texture, bind it
            //System.out.println( m._name + "  -- texid: " + m._material._texId );
            if (m._material._texId > 0) {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, m._material._texId);
            } else {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }

            if (m._material != null) {
                GL11.glEnable(GL11.GL_COLOR_MATERIAL);
                ByteBuffer temp = ByteBuffer.allocateDirect(16);
                temp.order(ByteOrder.nativeOrder());
                GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT, (FloatBuffer) temp.asFloatBuffer().put(new float[]{m._material._ambient[0], m._material._ambient[1], m._material._ambient[2], 1.0f}).flip());
                GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_DIFFUSE, (FloatBuffer) temp.asFloatBuffer().put(new float[]{m._material._diffuse[0], m._material._diffuse[1], m._material._diffuse[2], 1.0f}).flip());
                GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_SPECULAR, (FloatBuffer) temp.asFloatBuffer().put(new float[]{m._material._specular[0], m._material._specular[1], m._material._specular[2], 1.0f}).flip());
                GL11.glMaterialf(GL11.GL_FRONT_AND_BACK, GL11.GL_SHININESS, m._material._shininess);
            }

            // render triangles.. this is too basic. should be optimized
            GL11.glBegin(GL11.GL_TRIANGLES);

            //if( m._material._texId > 0 )
            //GL11.glColor4f( 1, 1, 1, 1 );
            //else
            if (m._material != null)
                GL11.glColor4f(m._material._diffuse[0], m._material._diffuse[1], m._material._diffuse[2], 1.0f);
            for (int fi = 0; fi < m._faceList.size(); fi++) {
                ObjImpFace f = (ObjImpFace) m._faceList.get(fi);

                GL11.glNormal3f(_normalList.get(f._na)[0], _normalList.get(f._na)[1], _normalList.get(f._na)[2]);
                if (m._material._texId > 0) GL11.glTexCoord2f(_texcoordList.get(f._ta)[0], _texcoordList.get(f._ta)[1]);
                GL11.glVertex3f(_vertexList.get(f._a)[0], _vertexList.get(f._a)[1], _vertexList.get(f._a)[2]);

                GL11.glNormal3f(_normalList.get(f._nb)[0], _normalList.get(f._nb)[1], _normalList.get(f._nb)[2]);
                if (m._material._texId > 0) GL11.glTexCoord2f(_texcoordList.get(f._tb)[0], _texcoordList.get(f._tb)[1]);
                GL11.glVertex3f(_vertexList.get(f._b)[0], _vertexList.get(f._b)[1], _vertexList.get(f._b)[2]);

                GL11.glNormal3f(_normalList.get(f._nc)[0], _normalList.get(f._nc)[1], _normalList.get(f._nc)[2]);
                if (m._material._texId > 0) GL11.glTexCoord2f(_texcoordList.get(f._tc)[0], _texcoordList.get(f._tc)[1]);
                GL11.glVertex3f(_vertexList.get(f._c)[0], _vertexList.get(f._c)[1], _vertexList.get(f._c)[2]);
            }

            GL11.glEnd();
        }

        if (_callListID > 0 && !_callListCompiled) {
            GL11.glEndList();
            _callListCompiled = true;
        }
    }

    public ObjImpMesh getMeshByIdx(int i) {
        return _meshList.get(i);
    }


    public ObjImpMesh getMeshByName(String name) {
        for (int i = 0; i < _meshList.size(); i++) {
            ObjImpMesh m = _meshList.get(i);
            if (m._name.equals(name))
                return _meshList.get(i);
        }

        return null;
    }

}
