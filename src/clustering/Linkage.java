package clustering;

/**
 * Created by samuelkolb on 31/10/14.
 *
 * @author Samuel Kolb
 */
public enum Linkage {

	SINGLE {
		@Override
		public double combine(double distance1, double distance2) {
			return Math.min(distance1, distance2);
		}
	}, COMPLETE {
		@Override
		public double combine(double distance1, double distance2) {
			return Math.max(distance1, distance2);
		}
	}, AVERAGE {
		@Override
		public double combine(double distance1, double distance2) {
			return (distance1 + distance2) / 2;
		}
	};

	public abstract double combine(double distance1, double distance2);
}
