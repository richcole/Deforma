package game.model;

import java.util.List;

import com.google.common.collect.Lists;

import game.math.Matrix;
import game.math.Vector;
import game.nwn.Lexicon;

public class MeshBuilder {
	private List<Vector> vs = Lists.newArrayList();
	private List<Vector> ns = Lists.newArrayList();
	private List<Integer> es = Lists.newArrayList();
	private List<Vector> ts = Lists.newArrayList();
	private List<Integer> is = Lists.newArrayList();
	private Lexicon images = new Lexicon();
	private Matrix tr;
	
	public MeshBuilder() {
		
	}
	
	public void addTri(String imageName, Vector va, Vector vb, Vector vc, Vector n, Vector ta, Vector tb, Vector tc) {
		int index = vs.size();
		vs.add(va);
		vs.add(vb);
		vs.add(vc);
		ns.add(n);
		ns.add(n);
		ns.add(n);
		ts.add(ta);
		ts.add(tb);
		ts.add(tc);
		es.add(index);
		es.add(index+1);
		es.add(index+2);
		is.add(images.getIndex(imageName));
		is.add(images.getIndex(imageName));
		is.add(images.getIndex(imageName));
	}

	public Mesh build() {
		Mesh mesh = new Mesh(vs.size(), es.size() / 3);
		mesh.nb = 1;
		for(int i=0; i<vs.size(); ++i) {
			Vector n = ns.get(i);
			Vector p = tr.times(vs.get(i));
			Vector np = tr.times(p.plus(n));
			Vector tn = np.minus(p).normalize();
			for(int c=0; c<3; ++c) {
				mesh.p1[i*3+c] = p.get(c);
				mesh.p2[i*3+c] = p.get(c);
				mesh.n[i*3+c] = tn.get(c);
			}
			for(int c=0; c<2; ++c) {
				mesh.t[i*2+c] = ts.get(i).get(c);
			}
			mesh.i[i] = is.get(i);
		}
		for(int i=0; i<es.size(); ++i) {
			mesh.e[i] = (int) es.get(i);
		}
		mesh.imageList = images.toList();
		return mesh;
	}

	public void setTr(Matrix tr) {
		this.tr = tr;
	}

}
