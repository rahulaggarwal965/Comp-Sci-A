package math;

public final class Collision {

	public static boolean pointPointIntersection(float x1, float y1, float x2, float y2) {
		if(x1 == x2 && y1 == y2) {
			return true;
		}
		return false;
	}
	
	public static boolean pointCircleIntersection(float x, float y, float cx, float cy, float r) {
		if(Maths.distance(cx, cy, x, y) <= r) {
				return true;
		}
		return false;
	}
	
	public static boolean circleCircleIntersection(float cx1, float cy1, float r1, float cx2, float cy2, float r2) {
		if(Maths.distance(cx1, cy1, cx2, cy2) <= r1 + r2) {
			return true;
		}
		return false;
	}
	
	public static boolean pointRectangleIntersection(float x1, float y1, float x2, float y2, float w, float h) {
		if(x1 >= x2 && x1 <= x2 + w && y1 >= y2 && y1 <= y2 + h) {
			return true;
		}
		return false;
	}
	
	public static boolean rectangleRectangleIntersection(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2) {
		if(x1 + w1 >= x2 && x1 <= x2 + w2 && y1 + h1 >= y2 && y1 <= y2 + h2) {
			return true;
		}
		return false;
	}
	
	public static boolean rectangleCircleIntersection(float x, float y, float w, float h, float cx, float cy, float r) {
		float tX = cx, tY = cy;
		if (cx < x) tX = x;
		else if(cx > x + w) tX = x + w;
		if (cy < y) tY = y;
		else if(cy> y + h) tY = y + h;
		if(Maths.distance(cx, cy, tX, tY) <= r) {
			return true;
		}
		return false;
	}
	
	public static int sideRectangleCircleIntersection(float x, float y, float w, float h, float cx, float cy, float r, float dx, float dy) {
		if (!rectangleCircleIntersection(x, y, w, h, cx, cy, r)) return -1;
		//R or L
		if(cy + r - dy > y && cy - r - dy < y + h) {
			//Left
			if(cx + r - dx < x) return 0;
			else return 2;
		// T or B
		} else {
			//Top
			if (cy + r - dy < y) return 1;
			else return 3;
		}
		
	}
	
	public static boolean linePointIntersection(float x1, float y1, float x2, float y2, float px, float py) {
		if(Math.abs(Maths.distance(x1, y1, px, py) + Maths.distance(x1, y1, px, py) - Maths.distance(x1, y1, x2, y2)) <= 0.1) {
			return true;
		}
		return false;
	}
	
	public static boolean lineCircleIntersection(float x1, float y1, float x2, float y2, float cx, float cy, float r) {
		if(pointCircleIntersection(x1, y1, cx, cy, r) || pointCircleIntersection(x2, y2, cx, cy, r)) {
			return true;
		}
		float projPercent = ((cx - x1)*(x2 - x1) + (cy - y1)*(y2-y1))/((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
		float pX = x1 + projPercent*(x2-x1);
		float pY = y1 + projPercent*(y2-y1);
		if(!(linePointIntersection(x1, y1, x2, y2, pX, pY))) {
			return false;
		}
		if(Maths.distance(pX, pY, cx, cy) <= r) {
			return true;
		}
		return false;
	}
	
	public static boolean lineLineIntersection (float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		  float h = ((x4 - x3)*(y1 - y3) - (y4 - y3)*(x1-x3)) / ((y4 - y3)*(x2-  x1) - (x4 - x3)*(y2 - y1));
		  float g = ((x2 - x1)*(y1 - y3) - (y2 - y1)*(x1-x3)) / ((y4 - y3)*(x2 - x1) - (x4 - x3)*(y2 - y1));
		  if (h >=  0 && h <= 1 && g >= 0 && g <= 1) {
			  return true;
		  }
		  return false;
	}
	
	public static boolean lineRectangleIntersection(float x1, float y1, float x2, float y2, float rx, float ry, float w, float h) {
		return lineLineIntersection(x1, y1, x2, y2, rx, ry, rx+w, ry) || lineLineIntersection(x1, y1, x2, y2, rx+w, ry, rx+w, ry+h) || lineLineIntersection(x1, y1, x2, y2, rx+w, ry+h, rx, ry+h) || lineLineIntersection(x1, y1, x2, y2, rx, ry+h, rx, ry);
	}
	
	public static boolean RayOBBIntersection(Vec3 rayOrigin, Vec3 rayDirection, Vec3 bbMin, Vec3 bbMax, Matrix world) {
		float tMin = 0.0f, tMax = 100000.0f;
		
		float[] matrix = world.getData();
		Vec3 bbPos = new Vec3(matrix[3], matrix[7], matrix[11]);
		Vec3 delta = bbPos._subtract(rayOrigin);
		
		{
			Vec3 xAxis = new Vec3(matrix[0], matrix[4], matrix[8]);
			float e = xAxis.dot(delta);
			float f = rayDirection.dot(xAxis);
			
			if(Math.abs(f) > 0.001f) {
				float t1 = (e + bbMin.x)/f;
				float t2 = (e + bbMax.x)/f;
				
				if(t1 > t2) {
					float temp = t1; t1 = t2; t2 = temp;
				}
				
				if(t2 < tMax) tMax = t2;
				if(t1 > tMin) tMin = t1;
				
				if(tMax < tMin) return false;
			} else {
				if(bbMin.x - e > 0.0f || bbMax.x - e < 0.0f) return false;
			}
		}
		
		{
			Vec3 yAxis = new Vec3(matrix[1], matrix[5], matrix[9]);
			float e = yAxis.dot(delta);
			float f = rayDirection.dot(yAxis);
			
			if(Math.abs(f) > 0.001f) {
				float t1 = (e + bbMin.y)/f;
				float t2 = (e + bbMax.y)/f;
				
				if(t1 > t2) {
					float temp = t1; t1 = t2; t2 = temp;
				}
				
				if(t2 < tMax) tMax = t2;
				if(t1 > tMin) tMin = t1;
				
				if(tMax < tMin) return false;
			} else {
				if(bbMin.y - e > 0.0f || bbMax.y - e < 0.0f) return false;
			}
		} 
		
		{
			Vec3 zAxis = new Vec3(matrix[2], matrix[6], matrix[10]);
			float e = zAxis.dot(delta);
			float f = rayDirection.dot(zAxis);
			
			if(Math.abs(f) > 0.001f) {
				float t1 = (e + bbMin.z)/f;
				float t2 = (e + bbMax.z)/f;
				
				if(t1 > t2) {
					float temp = t1; t1 = t2; t2 = temp;
				}
				
				if(t2 < tMax) tMax = t2;
				if(t1 > tMin) tMin = t1;
				
				if(tMax < tMin) return false;
			} else {
				if(bbMin.z - e > 0.0f || bbMax.z - e < 0.0f) return false;
			}
		}
		
		return true;
	}

}
