package com.brush.opengldemo.compute;

import android.content.Context;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;

import com.brush.opengldemo.ScriptC_physics;
import com.brush.opengldemo.database.BristleParameters;
import com.brush.opengldemo.globject.Bristle;
import com.brush.opengldemo.globject.Brush;


/**
 * 类关联到的js中的文件
 */
public class PhysicsCompute {

    private RenderScript renderScript;
    private ScriptC_physics script;   //资源文件的东阿虎

    private Allocation inBristleIndices;
    private Allocation inAllocationTop;
    private Allocation inAllocationBottom;
    private Allocation outAllocation;

    private Brush brush;

    float [] out;
    float [] inTop;
    float [] inBottom;

    public PhysicsCompute(Context context, Brush brush) {

        this.brush = brush;
        Bristle[] bristleArray = brush.getBristles();

        renderScript = RenderScript.create(context);
        script = new ScriptC_physics(renderScript);
//
        script.set_BRISTLE_BASE_LENGTH(Bristle.BASE_LENGTH);
        script.set_SEGMENTS_PER_BRISTLE(Brush.SEGMENTS_PER_BRISTLE);
        script.set_BRUSH_RADIUS_UPPER(Bristle.radiusUpper);
        script.set_script(script);

        int numBristles = brush.getBristles().length;
        int numSegments = Brush.SEGMENTS_PER_BRISTLE;

        int[] bristleIndices = new int[numBristles];
        for ( int i = 0; i < numBristles; i++) {
            bristleIndices[i] = i * 3;
        }

        inBristleIndices = Allocation.createSized(renderScript, Element.I32(renderScript), numBristles, Allocation.USAGE_SCRIPT | Allocation.USAGE_SHARED);
        inBristleIndices.copyFrom(bristleIndices);

        inTop = new float[3 * numBristles];
        inBottom  = new float[3 * numBristles];

        for(int i = 0; i < bristleArray.length; i++) {
            inTop[i * 3] = bristleArray[i].top.vector[0];
            inTop[i * 3 + 1] = bristleArray[i].top.vector[1];
            inTop[i * 3 + 2] = bristleArray[i].top.vector[2];

            inBottom[i * 3] = bristleArray[i].bottom.vector[0];
            inBottom[i * 3 + 1] = bristleArray[i].bottom.vector[1];
            inBottom[i * 3 + 2] = bristleArray[i].bottom.vector[2];
        }

        inAllocationTop = Allocation.createSized(renderScript, Element.F32(renderScript), 3 * numBristles, Allocation.USAGE_SCRIPT | Allocation.USAGE_SHARED);
        inAllocationBottom = Allocation.createSized(renderScript, Element.F32(renderScript), 3 * numBristles, Allocation.USAGE_SCRIPT | Allocation.USAGE_SHARED);

        inAllocationTop.copyFrom(inTop);
        inAllocationBottom.copyFrom(inBottom);

        outAllocation = Allocation.createSized(renderScript, Element.F32(renderScript), 3 * 2 * numSegments * numBristles, Allocation.USAGE_SCRIPT | Allocation.USAGE_SHARED);
        out = new float[3 * 2 * numSegments * numBristles];

        script.bind_inBristlePositionTop(inAllocationTop);
        script.bind_inBristlePositionBottom(inAllocationBottom);
        script.bind_outBristlePosition(outAllocation);
    }

    public float[] computeVertexData() {

        //Set all parameters for compute script
        script.set_brushPosition(brush.getPosition().getFloat3());

        BristleParameters bristleParameters = brush.getBristleParameters();

        script.set_planarDistanceFromHandle(bristleParameters.planarDistanceFromHandle);
        script.set_upperControlPointLength(bristleParameters.upperControlPointLength);
        script.set_lowerControlPointLength(bristleParameters.lowerControlPointLength);

        script.set_brushHorizontalAngle((float) Math.toRadians(brush.getHorizontalAngle()));
        script.set_bristleHorizontalMaxAngle((float) Math.toRadians(bristleParameters.bristleHorizontalAngle));

        // Computes all positions
        script.invoke_compute(inBristleIndices);

        // Wait for script to complete before continuing
        renderScript.finish();

        outAllocation.copyTo(out);
        return out;
    }

    public void destroy() {
        inAllocationTop.destroy();
        inAllocationBottom.destroy();
        outAllocation.destroy();
        script.destroy();
        renderScript.destroy();
    }
}
