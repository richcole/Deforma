package component;

import game.math.Vector;
import lombok.Getter;
import lombok.Value;

@Value
@Getter
public class Dimension {

	public static final Dimension D1 = new Dimension(Vector.U3, Vector.U2);
	public static final Dimension D2 = new Dimension(Vector.U1, Vector.U3);
	public static final Dimension D3 = new Dimension(Vector.U1, Vector.U2);

	public static final Dimension MD1 = new Dimension(Vector.M3, Vector.U2);
	public static final Dimension MD2 = new Dimension(Vector.M1, Vector.U3);
	public static final Dimension MD3 = new Dimension(Vector.M1, Vector.U2);

	public static final Dimension[] POS_DIMS = {
		D1, D2, D3
	};

	public static final Dimension[] ALL_DIMS = {
		D1, D2, D3, MD1, MD2, MD3
	};

	private final Vector fwd;
	private final Vector right;
	private final Vector up;

	/**
	 * fwd = up cross right
	 */
	public Dimension(Vector right, Vector up) {
		this.fwd = right.cross(up);
		this.right = right;
		this.up = up;
	}

}
