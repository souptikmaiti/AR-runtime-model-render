package com.example.runtimemodels;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import android.os.Bundle;
import android.widget.Button;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {
    private ArFragment arFragment;
    private Button btnCube, btnSphere, btnCylinder;
    private enum ShapeType{
        CUBE,
        SPHERE,
        CYLINDER
    }
    private ShapeType shapeType= ShapeType.CUBE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment= (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ar_fragment);
        btnCube= findViewById(R.id.btn_cube);
        btnSphere= findViewById(R.id.btn_sphere);
        btnCylinder= findViewById(R.id.btn_cylinder);

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            Anchor anchor= hitResult.createAnchor();
            switch (shapeType){
                case CUBE: placeCube(anchor);
                    break;
                case SPHERE: placeSphere(anchor);
                    break;
                case CYLINDER: placeCylinder(anchor);
                    break;
            }

        });

        btnCube.setOnClickListener(view -> shapeType= ShapeType.CUBE);
        btnSphere.setOnClickListener(view -> shapeType= ShapeType.SPHERE);
        btnCylinder.setOnClickListener(view -> shapeType= ShapeType.CYLINDER);
    }

    private void placeCube(Anchor anchor) {
        MaterialFactory
                .makeOpaqueWithColor(this, new Color(android.graphics.Color.BLUE))
                .thenAccept(material -> {
                    ModelRenderable modelRenderable=
                            ShapeFactory.makeCube(new Vector3(.1f,.1f,.1f), new Vector3(0f,0f,0f),material);
                    addModelToScene(anchor, modelRenderable);
                });
    }

    private void placeSphere(Anchor anchor) {
        MaterialFactory
                .makeOpaqueWithColor(this, new Color(android.graphics.Color.RED))
                .thenAccept(material -> {
                   ModelRenderable modelRenderable=
                           ShapeFactory.makeSphere(0.1f, new Vector3(0f, 0f, 0f), material);
                    addModelToScene(anchor, modelRenderable);
                });
    }

    private void placeCylinder(Anchor anchor) {
        MaterialFactory
                .makeOpaqueWithColor(this, new Color(android.graphics.Color.GREEN))
                .thenAccept(material -> {
                   ModelRenderable modelRenderable=
                           ShapeFactory.makeCylinder(0.1f,0.4f, new Vector3(0f,0f,0f), material);
                   addModelToScene(anchor, modelRenderable);

                });
    }

    private void addModelToScene(Anchor anchor, ModelRenderable modelRenderable) {
        AnchorNode anchorNode= new AnchorNode(anchor);
        TransformableNode transformableNode= new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setRenderable(modelRenderable);
        transformableNode.setParent(anchorNode);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
    }
}
