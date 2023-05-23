package com.app.deliforcemanager.CustomViews.SignatureView.utils;


import org.jetbrains.annotations.NotNull;

/**
 * Build a SVG path as a string.
 *
 * https://www.w3.org/TR/SVGTiny12/paths.html
 */
public class SvgPathBuilder {

    private static final Character SVG_RELATIVE_CUBIC_BEZIER_CURVE = 'c';
    private static final Character SVG_MOVE = 'M';
    private final StringBuilder mStringBuilder;
    private final Integer mStrokeWidth;
    private final SvgPoint mStartPoint;
    private SvgPoint mLastPoint;

    SvgPathBuilder(final SvgPoint startPoint, final Integer strokeWidth) {
        mStrokeWidth = strokeWidth;
        mStartPoint = startPoint;
        mLastPoint = startPoint;
        mStringBuilder = new StringBuilder();
        mStringBuilder.append(SVG_RELATIVE_CUBIC_BEZIER_CURVE);

    }

    final Integer getStrokeWidth() {
        return mStrokeWidth;
    }

    final SvgPoint getLastPoint() {
        return mLastPoint;
    }

    SvgPathBuilder append(final SvgPoint controlPoint1, final SvgPoint controlPoint2, final SvgPoint endPoint) {
        mStringBuilder.append(makeRelativeCubicBezierCurve(controlPoint1, controlPoint2, endPoint));
        mLastPoint = endPoint;
        return this;
    }

    @NotNull
    @Override
    public String toString() {
        return "<path " +
                "stroke-width=\"" +
                mStrokeWidth +
                "\" " +
                "d=\"" +
                SVG_MOVE +
                mStartPoint +
                mStringBuilder +
                "\"/>";
    }

    private String makeRelativeCubicBezierCurve(final SvgPoint controlPoint1, final SvgPoint controlPoint2, final SvgPoint endPoint) {
        final String sControlPoint1 = controlPoint1.toRelativeCoordinates(mLastPoint);
        final String sControlPoint2 = controlPoint2.toRelativeCoordinates(mLastPoint);
        final String sEndPoint = endPoint.toRelativeCoordinates(mLastPoint);

        // discard zero curve
        final String svg = sControlPoint1 +
                " " +
                sControlPoint2 +
                " " +
                sEndPoint +
                " ";
        if ("c0 0 0 0 0 0".equals(svg)) {
            return "";
        } else {
            return svg;
        }
    }
}