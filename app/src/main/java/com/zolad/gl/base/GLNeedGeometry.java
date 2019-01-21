package com.zolad.gl.base;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


public class GLNeedGeometry {
    private static final float preAngle = 1f;
    private RectF mViewPortGLBounds;

    public GeometryArrays generateVertexData(float radius, RectF viewPortGLBounds, Point viewPortPxSize) {
        return generateVertexData(radius, viewPortGLBounds, viewPortPxSize, 0f);
    }

    /**
     * Generates a {@link GeometryArrays} object with arrays containing the resulting geometry
     * vertices and the corresponding triangle indexes.
     *
     * @param viewPortGLBounds the bounds of the GL viewport in GL scalar units.
     * @param viewPortPxSize   the size of the view port in pixels.
     * @param z                the z coordinate for the z-plane geometry.
     * @return an object with the resulting geometry.
     */
    public GeometryArrays generateVertexData(float radius, RectF viewPortGLBounds, Point viewPortPxSize, float z) {
        if (radius < viewPortPxSize.x / 2) {
            return new GeometryArrays(new float[]{viewPortGLBounds.left, viewPortGLBounds.top, z, 0, 1,
                    viewPortGLBounds.right, viewPortGLBounds.top, z, 1, 1,
                    viewPortGLBounds.left, viewPortGLBounds.bottom, z, 0, 0,
                    viewPortGLBounds.right, viewPortGLBounds.bottom, z, 1, 0,},
                    new short[]{0, 1, 2, 1, 2, 3});
        }
        this.mViewPortGLBounds = viewPortGLBounds;
        float r = radius / viewPortPxSize.x * viewPortGLBounds.width();
        //计算中心点
        PointF center = new PointF(0, 0);
        center.y = (float) (-Math.sqrt(Math.pow(r, 2) - Math.pow(viewPortGLBounds.right, 2)) + viewPortGLBounds.bottom);
        /*计算没增加一定角度时对应弧线上的坐标*/
        List<PointF> verticeList = new ArrayList<>();
        //起始角度
        float startAngle = (float) Math.toDegrees(Math.asin((viewPortGLBounds.bottom - center.y) / r));
        float endAngle = 180 - startAngle;
        float hy = center.y + r;//弧形顶点
        //右下
        verticeList.add(new PointF(viewPortGLBounds.right, viewPortGLBounds.bottom));
        for (float i = startAngle + preAngle; i < endAngle; i += preAngle) {
            if (i > 90 && i < 90 + preAngle) {
                verticeList.add(new PointF(0, hy));
            }
            PointF p = new PointF();
            p.x = (float) (center.x + r * Math.cos(Math.toRadians(i)));
            p.y = (float) (center.y + r * Math.sin(Math.toRadians(i)));
            verticeList.add(p);
        }
        //左下
        verticeList.add(new PointF(viewPortGLBounds.left, viewPortGLBounds.bottom));


        List<Float> verticeData = new ArrayList<>();
        //0左上
        verticeData.add(viewPortGLBounds.left);
        verticeData.add(viewPortGLBounds.top);
        verticeData.add(z);
        verticeData.add(computeIndice(viewPortGLBounds.left, false));
        verticeData.add(computeIndice(viewPortGLBounds.top, true));
        //1弧线顶点左下
        verticeData.add(viewPortGLBounds.left);
        verticeData.add(hy);
        verticeData.add(z);
        verticeData.add(computeIndice(viewPortGLBounds.left, false));
        verticeData.add(computeIndice(hy, true));
        //2右上
        verticeData.add(viewPortGLBounds.right);
        verticeData.add(viewPortGLBounds.top);
        verticeData.add(z);
        verticeData.add(computeIndice(viewPortGLBounds.right, false));
        verticeData.add(computeIndice(viewPortGLBounds.top, true));
        //3弧线顶点右下
        verticeData.add(viewPortGLBounds.right);
        verticeData.add(hy);
        verticeData.add(z);
        verticeData.add(computeIndice(viewPortGLBounds.right, false));
        verticeData.add(computeIndice(hy, true));
        //绘制顺序
        List<Short> indiceList = new ArrayList<>();
        indiceList.add( (short) 0 );
        indiceList.add( (short) 1 );
        indiceList.add( (short) 2 );
        indiceList.add( (short) 2 );
        indiceList.add( (short) 1 );
        indiceList.add( (short) 3 );
        short fix = 1;
        final int dataCount = verticeData.size() / 5;
        final int listCount = verticeList.size() - 1;
        for (int i = listCount; i >= 0; i--) {
            verticeData.add(verticeList.get(i).x);
            verticeData.add(verticeList.get(i).y);
            verticeData.add(z);
            verticeData.add(computeIndice(verticeList.get(i).x, false));
            verticeData.add(computeIndice(verticeList.get(i).y, true));
            if (i > 0) {
                if (verticeList.get(i).x == 0) {
                    fix = 3;
                }
                indiceList.add(fix);
                indiceList.add((short) (dataCount + listCount - i));
                indiceList.add((short) (dataCount + listCount - i + 1));
            }
        }
        float[] verticeArray = new float[verticeData.size()];
        for (int i = 0; i < verticeArray.length; i++) {
            verticeArray[i] = verticeData.get(i);
        }
        short[] indiceArray = new short[indiceList.size()];
        for (int i = 0; i < indiceArray.length; i++) {
            indiceArray[i] = indiceList.get(i);
        }
        return new GeometryArrays(verticeArray, indiceArray);
    }

    private float computeIndice(float value, boolean vertical) {
        if (vertical) {
            float height = Math.abs(mViewPortGLBounds.height());
            return (height / 2 + value) / height;
        } else {
            float width = mViewPortGLBounds.width();
            return (width / 2 + value) / width;
        }
    }

    private void addTriangle(GeometryArrays geoArrays, float[] tgleft1, float[] tgleft2, float[] tgleft3, RectF viewPort, float z) {

        final float[] vertices = geoArrays.triangleVertices;
        final short[] indices = geoArrays.triangleIndices;
        final int indicesOffset = geoArrays.indicesOffset;
        final int verticesOffset = geoArrays.verticesOffset;
        int rectPointIdx = 0;
        final int currentVertexOffset = verticesOffset;
        vertices[currentVertexOffset] = tgleft1[0];
        vertices[currentVertexOffset + 1] = tgleft1[1];
        vertices[currentVertexOffset + 2] = z;

        // UV (texture mapping)
        vertices[currentVertexOffset + 3] = (tgleft1[0] - viewPort.left) / viewPort.width();
        vertices[currentVertexOffset + 4] = (tgleft1[1] - viewPort.bottom) / -viewPort.height();

        vertices[currentVertexOffset + 5] = tgleft2[0];
        vertices[currentVertexOffset + 6] = tgleft2[1];
        vertices[currentVertexOffset + 7] = z;

        // UV (texture mapping)
        vertices[currentVertexOffset + 8] = (tgleft2[0] - viewPort.left) / viewPort.width();
        vertices[currentVertexOffset + 9] = (tgleft2[1] - viewPort.bottom) / -viewPort.height();

        vertices[currentVertexOffset + 10] = tgleft3[0];
        vertices[currentVertexOffset + 11] = tgleft3[1];
        vertices[currentVertexOffset + 12] = z;

        // UV (texture mapping)
        vertices[currentVertexOffset + 13] = (tgleft3[0] - viewPort.left) / viewPort.width();
        vertices[currentVertexOffset + 14] = (tgleft3[1] - viewPort.bottom) / -viewPort.height();


        final int initialIdx = verticesOffset / 5;
        indices[indicesOffset] = (short) (initialIdx);
        indices[indicesOffset + 1] = (short) (initialIdx + 1);
        indices[indicesOffset + 2] = (short) (initialIdx + 2);

    }

    /**
     * Adds the vertices of a rectangle defined by 4 corner points. The array of vertices passed
     * in must have the required length to add the geometry points (5 floats for each vertex). Also
     * the coordinates of the rect corners should already be in the view port space.
     *
     * @param geoArrays  an object containing the vertex and index data arrays and their current
     *                   offsets.
     * @param rectPoints an array of corner points defining the rectangle. index 0 is the x
     *                   coordinate and index 1 the y coordinate.
     * @param viewPort   the bounds of the current GL viewport, this is used to calculate the texture
     *                   mapping.
     * @param z          the z coordinate.
     */
    private void addRect(@NonNull GeometryArrays geoArrays, @NonNull float[][] rectPoints,
                         @NonNull RectF viewPort, float z) {
        final float[] vertices = geoArrays.triangleVertices;
        final short[] indices = geoArrays.triangleIndices;
        final int indicesOffset = geoArrays.indicesOffset;
        final int verticesOffset = geoArrays.verticesOffset;
        int rectPointIdx = 0;
        for (final float[] rectPoint : rectPoints) {
            // 5 values [xyzuv] per vertex
            final int currentVertexOffset = verticesOffset + rectPointIdx * 5;

            // XYZ (vertex space coordinates
            vertices[currentVertexOffset] = rectPoint[0];
            vertices[currentVertexOffset + 1] = rectPoint[1];
            vertices[currentVertexOffset + 2] = z;

            // UV (texture mapping)
            vertices[currentVertexOffset + 3] = (rectPoint[0] - viewPort.left) / viewPort.width();
            vertices[currentVertexOffset + 4] = (rectPoint[1] - viewPort.bottom) / -viewPort.height();

            rectPointIdx++;
        }

        // Index our triangles -- tell where each triangle vertex is
        final int initialIdx = verticesOffset / 5;
        indices[indicesOffset] = (short) (initialIdx);
        indices[indicesOffset + 1] = (short) (initialIdx + 1);
        indices[indicesOffset + 2] = (short) (initialIdx + 2);
        indices[indicesOffset + 3] = (short) (initialIdx + 1);
        indices[indicesOffset + 4] = (short) (initialIdx + 2);
        indices[indicesOffset + 5] = (short) (initialIdx + 3);
    }

    /**
     * Adds the vertices of a number of triangles to form a rounded corner. The triangles start at
     * some center point and will sweep from a given initial angle up to a final one. The size of
     * the triangles is defined by the radius.
     * <p>
     * The array of vertices passed in must have the required length to add the geometry points
     * (5 floats for each vertex). Also the coordinates of the rect corners should already be in
     * the view port space.
     *
     * @param geoArrays an object containing the vertex and index data arrays and their current
     *                  offsets.
     * @param center    the center point where all triangles will start.
     * @param radius    the desired radius in the x and y axis, in viewport dimensions.
     * @param rads0     the initial angle.
     * @param rads1     the final angle.
     * @param triangles the amount of triangles to create.
     * @param viewPort  the bounds of the current GL viewport, this is used to calculate the texture
     *                  mapping.
     * @param z         the z coordinate.
     */
    private void addRoundedCorner(@NonNull GeometryArrays geoArrays, @NonNull float[] center,
                                  float[] radius, float rads0, float rads1, int triangles, @NonNull RectF viewPort, float z) {
        final float[] vertices = geoArrays.triangleVertices;
        final short[] indices = geoArrays.triangleIndices;
        final int verticesOffset = geoArrays.verticesOffset;
        final int indicesOffset = geoArrays.indicesOffset;
        for (int i = 0; i < triangles; i++) {
            // final int currentOffset = verticesOffset + i * 15 /* each triangle is 3 * xyzuv */;
            final int currentOffset = verticesOffset + i * 5 + (i > 0 ? 2 * 5 : 0);
            final float rads = rads0 + (rads1 - rads0) * (i / (float) triangles);
            final float radsNext = rads0 + (rads1 - rads0) * ((i + 1) / (float) triangles);
            final int triangleEdge2Offset;

            if (i == 0) {
                // XYZUV - center point
                vertices[currentOffset] = center[0];
                vertices[currentOffset + 1] = center[1];
                vertices[currentOffset + 2] = z;
                vertices[currentOffset + 3] = (vertices[currentOffset] - viewPort.left) / viewPort.width();
                vertices[currentOffset + 4] =
                        (vertices[currentOffset + 1] - viewPort.bottom) / -viewPort.height();

                // XYZUV - triangle edge 1
                vertices[currentOffset + 5] = center[0] + radius[0] * (float) Math.cos(rads);
                vertices[currentOffset + 6] = center[1] + radius[1] * (float) Math.sin(rads);
                vertices[currentOffset + 7] = z;
                vertices[currentOffset + 8] =
                        (vertices[currentOffset + 5] - viewPort.left) / viewPort.width();
                vertices[currentOffset + 9] =
                        (vertices[currentOffset + 6] - viewPort.bottom) / -viewPort.height();

                triangleEdge2Offset = 10;
            } else {
                triangleEdge2Offset = 0;
            }

            // XYZUV - triangle edge 2
            final int edge2Offset = currentOffset + triangleEdge2Offset;
            vertices[edge2Offset] = center[0] + radius[0] * (float) Math.cos(radsNext);
            vertices[edge2Offset + 1] = center[1] + radius[1] * (float) Math.sin(radsNext);
            vertices[edge2Offset + 2] = z;
            vertices[edge2Offset + 3] = (vertices[edge2Offset] - viewPort.left) / viewPort.width();
            vertices[edge2Offset + 4] =
                    (vertices[edge2Offset + 1] - viewPort.bottom) / -viewPort.height();

            // Index our triangles -- tell where each triangle vertex is
            final int initialIdx = verticesOffset / 5;
            indices[indicesOffset + i * 3] = (short) (initialIdx);
            indices[indicesOffset + i * 3 + 1] = (short) (initialIdx + i + 1);
            indices[indicesOffset + i * 3 + 2] = (short) (initialIdx + i + 2);
        }
    }

    public static class GeometryArrays {
        public float[] triangleVertices;
        public short[] triangleIndices;
        public int verticesOffset = 0;
        public int indicesOffset = 0;

        public GeometryArrays(@NonNull float[] vertices, @NonNull short[] indices) {
            triangleVertices = vertices;
            triangleIndices = indices;
        }
    }
}