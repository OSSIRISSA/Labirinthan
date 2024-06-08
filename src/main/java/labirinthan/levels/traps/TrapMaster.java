package labirinthan.levels.traps;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import labirinthan.Labirinthan;
import labirinthan.character.MainCharacter;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrapMaster extends Node {

    private static final float SPIKE_DAMAGE = 0.6f;
    private static final float MINE_DAMAGE = 0.5f;
    private static final float MINE_RADIUS = 10f;
    private Spatial trapMesh;
    private final Labirinthan app;
    private final Node rootNode;
    private final Node interactionAreaNode;
    private TrapInteractionArea interactionArea;
    public TrapType trapType;
    private final ScheduledExecutorService scheduler;
    private final ScheduledExecutorService afterExplosion;

    private boolean isDropping;
    private final Vector3f endPosition;
    private float speed;
    private boolean isTriggered;

    public TrapMaster(Labirinthan application, AssetManager assetManager, Node localRootNode, float px, float py, float pz, int direction) {
        this.app = application;
        this.rootNode = localRootNode;

        Random random = new Random();

        if (random.nextInt(2) != 0){
            this.trapType=TrapType.SPIKE;
        } else this.trapType=TrapType.MINE;


        localRootNode.attachChild(this);
        this.setLocalTranslation(px, py + 3f, pz);
        switch (direction) {
            case 1 -> this.move(0, 0, -1);
            case 2 -> this.move(1, 0, 0);
            case 3 -> this.move(0, 0, 1);
            case 4 -> this.move(-1, 0, 0);
        }

        this.endPosition = this.getLocalTranslation().add(0, -5.85f, 0);

        switch (trapType) {
            case MINE -> {
                this.trapMesh = assetManager.loadModel("Models/Traps/mine.glb");
                trapMesh.scale(0.08f);
                trapMesh.move(0, -3f, 0);
            }
            case SPIKE -> {
                this.trapMesh = assetManager.loadModel("Models/Traps/spike.glb");
                trapMesh.scale(2f);
                trapMesh.rotate(0, 0, FastMath.PI);
                trapMesh.move(0, 6 - 3.05f, 0);

                if (random.nextInt(2) != 0) {
                    Spatial decoration = assetManager.loadModel("Models/Compositions/wolf_coins.glb");
                    decoration.move(0, -3 + 0.05f, 0);
                    decoration.scale(0.0015f);
                    this.attachChild(decoration);
                }

                // Initialize drop animation parameters
                Vector3f startPosition = this.getLocalTranslation();
                this.speed = 10f;
                this.isDropping = false;
                this.setLocalTranslation(startPosition);
            }
        }
        this.attachChild(trapMesh);

        interactionAreaNode = new Node("InteractionArea");
        this.attachChild(interactionAreaNode);
        interactionArea = new TrapInteractionArea(this, new Vector3f(1f, 3f, 1f));
        interactionArea.setPosition(this.getWorldTranslation());
        app.bulletAppState.getPhysicsSpace().add(interactionArea);

        scheduler = Executors.newScheduledThreadPool(1);
        afterExplosion = Executors.newScheduledThreadPool(1);
    }

    public void startDropAnimation() {
        if (trapType == TrapType.SPIKE) {
            Labirinthan.level.playSpikeSound(interactionAreaNode);
            this.isDropping = true;
        }
    }

    @Override
    public void updateLogicalState(float tpf) {
        super.updateLogicalState(tpf);
        if (isDropping) {
            Vector3f currentPosition = this.getLocalTranslation();
            if (currentPosition.distance(endPosition) > speed * tpf) {
                Vector3f direction = endPosition.subtract(currentPosition).normalize();
                this.setLocalTranslation(currentPosition.add(direction.mult(speed * tpf)));
            } else {
                this.setLocalTranslation(endPosition);
                isDropping = false;
                damagePlayer();
            }
        }
    }

    private void damagePlayer() {
        if (isTriggered) return;
        isTriggered = true;

        switch (trapType) {
            case MINE -> {
                this.detachChild(trapMesh);
                float mineDamage = MainCharacter.characterNode.getWorldTranslation().distance(this.getWorldTranslation()) - MINE_RADIUS;
                if (mineDamage < 0) {
                    app.character.hpActions(mineDamage * MINE_DAMAGE);
                }
            }
            case SPIKE -> app.character.hpActions(-SPIKE_DAMAGE);
        }
        removeInteractionZone();
    }

    public void startMineExplode() {
        Labirinthan.level.playMineSound(interactionAreaNode);
        scheduler.schedule(() -> app.enqueue(this::explodeMine), 1, TimeUnit.SECONDS);
    }

    private void explodeMine() {
        Labirinthan.level.playMineExplodeSound(interactionAreaNode);
        ParticleEmitter explosionEffect = createExplosionEffect();
        explosionEffect.setLocalTranslation(this.getWorldTranslation().add(0,-3,0));
        rootNode.attachChild(explosionEffect);
        explosionEffect.emitAllParticles();
        explosionEffect.setParticlesPerSec(0);
        damagePlayer();

        afterExplosion.schedule(() -> app.enqueue(() -> {
            rootNode.detachChild(explosionEffect);
            return null;
        }), 2, TimeUnit.SECONDS);
    }

    private ParticleEmitter createExplosionEffect() {
        ParticleEmitter emitter = new ParticleEmitter("Explosion", ParticleMesh.Type.Triangle, 30);
        emitter.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
        emitter.setParticlesPerSec(0);
        emitter.setGravity(0, -5, 0);
        emitter.setLowLife(1f);
        emitter.setHighLife(3f);
        emitter.setStartSize(0.3f);
        emitter.setEndSize(0.1f);
        emitter.setStartColor(ColorRGBA.Orange);
        emitter.setEndColor(ColorRGBA.Red);
        emitter.setImagesX(1);
        emitter.setImagesY(1);
        emitter.getParticleInfluencer().setVelocityVariation(0.8f);

        Material material = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        material.setTexture("Texture", app.getAssetManager().loadTexture("Textures/explosion.png"));
        emitter.setMaterial(material);

        return emitter;
    }

    public void removeInteractionZone() {
        if (interactionAreaNode != null) {
            app.bulletAppState.getPhysicsSpace().remove(interactionArea);
            interactionArea = null;
        }

    }
}
