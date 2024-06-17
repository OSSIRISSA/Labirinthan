/**
 * Task: Game
 * File: Decoration.java
 *
 *  @author Iryna Hryshchenko
 */
package labirinthan.levels.parts;

import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.Random;

/**
 * Decoration constructor
 */
public class Decoration extends Mesh {

    /**
     * Decoration constructor
     * @param direction - face direction
     * @param assetManager - asset manager
     * @param localRootNode - localRootNode
     * @param px - x
     * @param pz - z
     */
    public Decoration(float direction, AssetManager assetManager, Node localRootNode, float px, float pz) {
        Random random = new Random();
        Spatial decoration = null;
        int decorOption = random.nextInt(17);
        switch (decorOption) {
            case 0 -> {
                decoration = assetManager.loadModel("Models/Decorations/happy_skeleton.glb");
                decoration.setLocalTranslation(px, 0.3f, pz);
                decoration.scale(1.7f);
            }
            case 1 -> {
                decoration = assetManager.loadModel("Models/Decorations/alchemist_table.glb");
                decoration.setLocalTranslation(px, 0, pz);
                decoration.scale(0.03f);
                decoration.rotate(0, -FastMath.HALF_PI, 0);
            }
            case 2 -> {
                if(random.nextInt(2)!=0){
                    decoration = assetManager.loadModel("Models/Compositions/candle_holder.glb");
                    switch ((int) direction) {
                        case 1 -> decoration.setLocalTranslation(px+0.7f, 0.57f, pz-1.5f);
                        case 2 -> decoration.setLocalTranslation(px+1.5f, 0.57f, pz+0.7f);
                        case 3 -> decoration.setLocalTranslation(px-0.7f, 0.57f, pz+1.5f);
                        default -> decoration.setLocalTranslation(px-1.5f, 0.57f, pz-0.7f);
                    }
                    decoration.rotate(0, FastMath.QUARTER_PI, 0);
                    decoration.scale(0.3f);
                    decoration.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
                    decoration.setShadowMode(RenderQueue.ShadowMode.Receive);
                    localRootNode.attachChild(decoration);
                }

                decoration = assetManager.loadModel("Models/Decorations/baphomet_statue.glb");
                switch ((int) direction) {
                    case 1 -> decoration.setLocalTranslation(px - 2, 1.8f, pz);
                    case 2 -> decoration.setLocalTranslation(px, 1.8f, pz - 2);
                    case 3 -> decoration.setLocalTranslation(px + 2, 1.8f, pz);
                    default -> decoration.setLocalTranslation(px, 1.8f, pz + 2);
                }
                decoration.rotate(0, FastMath.PI, 0);
                decoration.scale(0.45f);
            }
            case 3 -> {
                decoration = assetManager.loadModel("Models/Decorations/devil_gargoyle_sitting_on_globe.glb");
                decoration.setLocalTranslation(px, 0, pz);
                decoration.rotate(0, FastMath.PI, 0);
                decoration.scale(13);
            }
            case 4 -> {
                if(random.nextInt(2)!=0) {
                    decoration = assetManager.loadModel("Models/Compositions/wolf_coins.glb");
                    switch ((int) direction) {
                        case 1 -> {
                            decoration.setLocalTranslation(px + 1.15f, 0, pz - 1);
                            decoration.rotate(0, FastMath.HALF_PI, 0);
                        }
                        case 2 -> decoration.setLocalTranslation(px + 1, 0, pz + 1.15f);
                        case 3 -> {
                            decoration.setLocalTranslation(px - 1.15f, 0, pz + 1);
                            decoration.rotate(0, -FastMath.HALF_PI, 0);
                        }
                        default -> {
                            decoration.setLocalTranslation(px - 1, 0, pz - 1.15f);
                            decoration.rotate(0, FastMath.PI, 0);
                        }
                    }
                    decoration.scale(0.0015f);
                    decoration.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
                    decoration.setShadowMode(RenderQueue.ShadowMode.Receive);
                    localRootNode.attachChild(decoration);
                }

                if(random.nextInt(2)!=0) {
                    decoration = assetManager.loadModel("Models/Compositions/wolf_coins.glb");
                    switch ((int) direction) {
                        case 1 -> {
                            decoration.setLocalTranslation(px - 1.5f, 0, pz);
                            decoration.rotate(0, -FastMath.HALF_PI, 0);
                        }
                        case 2 -> {
                            decoration.setLocalTranslation(px, 0, pz - 1.5f);
                            decoration.rotate(0, FastMath.PI, 0);
                        }
                        case 3 -> {
                            decoration.setLocalTranslation(px + 1.5f, 0, pz);
                            decoration.rotate(0, FastMath.HALF_PI, 0);
                        }
                        default -> decoration.setLocalTranslation(px, 0, pz + 1.5f);
                    }
                    decoration.scale(0.0015f);
                    decoration.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
                    decoration.setShadowMode(RenderQueue.ShadowMode.Receive);
                    localRootNode.attachChild(decoration);
                }

                decoration = assetManager.loadModel("Models/Decorations/medevial_chest.glb");
                decoration.setLocalTranslation(px, 0.87f, pz);
                decoration.rotate(0, -FastMath.HALF_PI, 0);
                decoration.scale(1.5f);
            }
            case 5 -> {
                if(random.nextInt(2)!=0) {
                    decoration = assetManager.loadModel("Models/Compositions/key.glb");
                    switch ((int) direction) {
                        case 1 -> {
                            decoration.setLocalTranslation(px-0.3f, 0.025f, pz-1f);
                            decoration.rotate(0, -FastMath.QUARTER_PI, FastMath.HALF_PI);
                        }
                        case 2 -> {
                            decoration.setLocalTranslation(px+1f, 0.025f, pz-0.3f);
                            decoration.rotate(-FastMath.HALF_PI, -FastMath.QUARTER_PI, 0);
                        }
                        case 3 -> {
                            decoration.setLocalTranslation(px+0.3f, 0.025f, pz+1f);
                            decoration.rotate(0, -FastMath.QUARTER_PI, -FastMath.HALF_PI);
                        }
                        default -> {
                            decoration.setLocalTranslation(px-1f, 0.025f, pz+0.3f);
                            decoration.rotate(FastMath.HALF_PI, -FastMath.QUARTER_PI, 0);
                        }
                    }
                    switch ((int) direction) {
                        case 1 -> decoration.rotate(0, -FastMath.HALF_PI, 0);
                        case 2 -> decoration.rotate(0, FastMath.PI, 0);
                        case 3 -> decoration.rotate(0, FastMath.HALF_PI, 0);
                    }
                    decoration.scale(0.0007f);
                    decoration.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
                    decoration.setShadowMode(RenderQueue.ShadowMode.Receive);
                    localRootNode.attachChild(decoration);
                }

                decoration = assetManager.loadModel("Models/Decorations/medieval_era_chest.glb");
                decoration.setLocalTranslation(px, 0.5f, pz);
                decoration.scale(1);
                decoration.rotate(0, FastMath.HALF_PI, 0);
            }
            case 6 -> {
                decoration = assetManager.loadModel("Models/Decorations/monsters_skull_with_gold_backdrop.glb");
                switch ((int) direction) {
                    case 1 -> decoration.setLocalTranslation(px, 2.5f, pz+1.95f);
                    case 2 -> decoration.setLocalTranslation(px-1.95f, 2.5f, pz);
                    case 3 -> decoration.setLocalTranslation(px, 2.5f, pz-1.95f);
                    default -> decoration.setLocalTranslation(px+1.95f, 2.5f, pz);
                }
                decoration.rotate(0, -FastMath.HALF_PI, 0);
                decoration.scale(0.2f);
            }
            case 7 -> {
                decoration = assetManager.loadModel("Models/Decorations/occult_books.glb");
                switch ((int) direction) {
                    case 1 -> decoration.setLocalTranslation(px+0.2f, 0, pz);
                    case 2 -> decoration.setLocalTranslation(px, 0, pz+0.2f);
                    case 3 -> decoration.setLocalTranslation(px-0.2f, 0, pz);
                    default -> decoration.setLocalTranslation(px, 0, pz-0.2f);
                }
                decoration.rotate(0, FastMath.PI, 0);
                decoration.scale(0.002f);
            }
            case 8 -> {
                decoration = assetManager.loadModel("Models/Decorations/pentacle.glb");
                decoration.setLocalTranslation(px, -0.01f, pz);
                decoration.scale(0.015f);
            }
            case 9 -> {
                decoration = assetManager.loadModel("Models/Decorations/pirate_props_rum_and_barrels.glb");
                switch ((int) direction) {
                    case 1 -> decoration.setLocalTranslation(px-0.2f, 0.01f, pz);
                    case 2 -> decoration.setLocalTranslation(px, 0.01f, pz-0.2f);
                    case 3 -> decoration.setLocalTranslation(px+0.2f, 0.01f, pz);
                    default -> decoration.setLocalTranslation(px, 0.01f, pz+0.2f);
                }
                decoration.scale(0.7f);
            }
            case 10 -> {
                decoration = assetManager.loadModel("Models/Decorations/stack_of_old_books.glb");
                decoration.setLocalTranslation(px, 0.3f, pz);
                decoration.scale(1);
            }
            case 11 -> {
                decoration = assetManager.loadModel("Models/Compositions/veiled_bride_skull_bust.glb");
                switch ((int) direction) {
                    case 1 -> {
                        decoration.setLocalTranslation(px+0.5f, 0, pz);
                        decoration.rotate(0, -FastMath.HALF_PI, 0);
                    }
                    case 2 -> {
                        decoration.setLocalTranslation(px, 0, pz+0.5f);
                        decoration.rotate(0, FastMath.PI, 0);
                    }
                    case 3 -> {
                        decoration.setLocalTranslation(px-0.5f, 0, pz);
                        decoration.rotate(0, FastMath.HALF_PI, 0);
                    }
                    default -> decoration.setLocalTranslation(px, 0, pz-0.5f);
                }
                decoration.rotate(0, FastMath.PI, 0);
                decoration.rotate(0, FastMath.QUARTER_PI, 0);
                decoration.scale(5);
                decoration.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
                decoration.setShadowMode(RenderQueue.ShadowMode.Receive);
                localRootNode.attachChild(decoration);

                decoration = assetManager.loadModel("Models/Compositions/brokendragongoblet.glb");
                switch ((int) direction) {
                    case 1 -> {
                        decoration.setLocalTranslation(px-0.3f, 0.2f, pz);
                        decoration.rotate(0, -FastMath.HALF_PI, 0);
                    }
                    case 2 -> {
                        decoration.setLocalTranslation(px, 0.2f, pz-0.3f);
                        decoration.rotate(0, FastMath.PI, 0);
                    }
                    case 3 -> {
                        decoration.setLocalTranslation(px+0.3f, 0.2f, pz);
                        decoration.rotate(0, FastMath.HALF_PI, 0);
                    }
                    default -> decoration.setLocalTranslation(px, 0.2f, pz+0.3f);
                }
                decoration.scale(0.2f);
                decoration.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
                decoration.setShadowMode(RenderQueue.ShadowMode.Receive);
                localRootNode.attachChild(decoration);

                decoration = assetManager.loadModel("Models/Compositions/key.glb");
                switch ((int) direction) {
                    case 1 -> {
                        decoration.setLocalTranslation(px-0.3f, 0.025f, pz-0.4f);
                        decoration.rotate(0, FastMath.QUARTER_PI, FastMath.HALF_PI);
                    }
                    case 2 -> {
                        decoration.setLocalTranslation(px+0.4f, 0.025f, pz-0.3f);
                        decoration.rotate(-FastMath.HALF_PI, FastMath.QUARTER_PI, 0);
                    }
                    case 3 -> {
                        decoration.setLocalTranslation(px+0.3f, 0.025f, pz+0.4f);
                        decoration.rotate(0, FastMath.QUARTER_PI, -FastMath.HALF_PI);
                    }
                    default -> {
                        decoration.setLocalTranslation(px-0.4f, 0.025f, pz+0.3f);
                        decoration.rotate(FastMath.HALF_PI, FastMath.QUARTER_PI, 0);
                    }
                }
                decoration.scale(0.0007f);
            }
            case 12 -> {
                decoration = assetManager.loadModel("Models/Compositions/free_human_skull.glb");
                switch ((int) direction) {
                    case 1 -> {
                        decoration.setLocalTranslation(px+0.5f, 0.2f, pz);
                        decoration.rotate(0, -FastMath.HALF_PI, 0);
                    }
                    case 2 -> {
                        decoration.setLocalTranslation(px, 0.2f, pz+0.5f);
                        decoration.rotate(0, FastMath.PI, 0);
                    }
                    case 3 -> {
                        decoration.setLocalTranslation(px-0.5f, 0.2f, pz);
                        decoration.rotate(0, FastMath.HALF_PI, 0);
                    }
                    default -> decoration.setLocalTranslation(px, 0.2f, pz-0.5f);
                }
                decoration.rotate(0, FastMath.PI, 0);
                decoration.rotate(0, FastMath.QUARTER_PI, 0);
                decoration.scale(0.005f);
                decoration.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
                decoration.setShadowMode(RenderQueue.ShadowMode.Receive);
                localRootNode.attachChild(decoration);

                decoration = assetManager.loadModel("Models/Compositions/wolf_coins.glb");
                switch ((int) direction) {
                    case 1 -> {
                        decoration.setLocalTranslation(px+0.15f, 0, pz);
                        decoration.rotate(0, -FastMath.HALF_PI, 0);
                    }
                    case 2 -> {
                        decoration.setLocalTranslation(px, 0, pz+0.15f);
                        decoration.rotate(0, FastMath.PI, 0);
                    }
                    case 3 -> {
                        decoration.setLocalTranslation(px-0.15f, 0, pz);
                        decoration.rotate(0, FastMath.HALF_PI, 0);
                    }
                    default -> decoration.setLocalTranslation(px, 0, pz-0.15f);
                }
                decoration.scale(0.0015f);
                decoration.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
                decoration.setShadowMode(RenderQueue.ShadowMode.Receive);
                localRootNode.attachChild(decoration);

                decoration = assetManager.loadModel("Models/Compositions/wolf_coins.glb");
                switch ((int) direction) {
                    case 1 -> {
                        decoration.setLocalTranslation(px+1.15f, 0, pz-1);
                        decoration.rotate(0, FastMath.HALF_PI, 0);
                    }
                    case 2 -> decoration.setLocalTranslation(px+1, 0, pz+1.15f);
                    case 3 -> {
                        decoration.setLocalTranslation(px-1.15f, 0, pz+1);
                        decoration.rotate(0, -FastMath.HALF_PI, 0);
                    }
                    default -> {
                        decoration.setLocalTranslation(px-1, 0, pz-1.15f);
                        decoration.rotate(0, FastMath.PI, 0);
                    }
                }
                decoration.scale(0.0015f);
                decoration.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
                decoration.setShadowMode(RenderQueue.ShadowMode.Receive);
                localRootNode.attachChild(decoration);

                decoration = assetManager.loadModel("Models/Compositions/wolf_coins.glb");
                switch ((int) direction) {
                    case 1 -> {
                        decoration.setLocalTranslation(px+1.15f, 0, pz-1);
                        decoration.rotate(0, -FastMath.HALF_PI, 0);
                    }
                    case 2 -> {
                        decoration.setLocalTranslation(px+1, 0, pz+1.15f);
                        decoration.rotate(0, FastMath.PI, 0);
                    }
                    case 3 -> {
                        decoration.setLocalTranslation(px-1.15f, 0, pz+1);
                        decoration.rotate(0, +FastMath.HALF_PI, 0);
                    }
                    default -> decoration.setLocalTranslation(px-1, 0, pz-1.15f);
                }
                decoration.scale(0.0015f);
                decoration.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
                decoration.setShadowMode(RenderQueue.ShadowMode.Receive);
                localRootNode.attachChild(decoration);

                decoration = assetManager.loadModel("Models/Compositions/hurdy-gurdy.glb");
                switch ((int) direction) {
                    case 1 -> decoration.setLocalTranslation(px-1.5f, 0.3f, pz-1.8f);
                    case 2 -> decoration.setLocalTranslation(px+1.8f, 0.3f, pz-1.5f);
                    case 3 -> decoration.setLocalTranslation(px+1.5f, 0.3f, pz+1.8f);
                    default -> decoration.setLocalTranslation(px-1.8f, 0.3f, pz+1.5f);
                }
                decoration.scale(0.0042f);
            }
            case 13 -> {
                decoration = assetManager.loadModel("Models/Compositions/ugly_demon_mask_-_badly.glb");
                switch ((int) direction) {
                    case 1 -> {
                        decoration.setLocalTranslation(px-1.5f, -4.1f, pz);
                        decoration.rotate(0, -FastMath.HALF_PI, 0);
                    }
                    case 2 -> {
                        decoration.setLocalTranslation(px, -4.1f, pz-1.5f);
                        decoration.rotate(0, FastMath.PI, 0);
                    }
                    case 3 -> {
                        decoration.setLocalTranslation(px+1.5f, -4.1f, pz);
                        decoration.rotate(0, FastMath.HALF_PI, 0);
                    }
                    default -> decoration.setLocalTranslation(px, -4.1f, pz+1.5f);
                }
                decoration.rotate(0, FastMath.PI, 0);
                decoration.rotate(0, FastMath.QUARTER_PI, 0);
                decoration.scale(0.3f);
                decoration.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
                decoration.setShadowMode(RenderQueue.ShadowMode.Receive);
                localRootNode.attachChild(decoration);

                decoration = assetManager.loadModel("Models/Compositions/candle_holder.glb");
                switch ((int) direction) {
                    case 1 -> decoration.setLocalTranslation(px-0.7f, 0.75f, pz+1);
                    case 2 -> decoration.setLocalTranslation(px-1, 0.75f, pz-0.7f);
                    case 3 -> decoration.setLocalTranslation(px+0.7f, 0.75f, pz-1);
                    default -> decoration.setLocalTranslation(px+1, 0.75f, pz+0.7f);
                }
                decoration.rotate(0, FastMath.PI, 0);
                decoration.rotate(0, FastMath.QUARTER_PI, 0);
                decoration.scale(0.4f);
                decoration.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
                decoration.setShadowMode(RenderQueue.ShadowMode.Receive);
                localRootNode.attachChild(decoration);
            }
            case 14 -> {
                decoration = assetManager.loadModel("Models/Decorations/grieving_angel.glb");
                switch ((int) direction) {
                    case 1 -> {
                        decoration.setLocalTranslation(px+7.2f, 8.7f, pz-2);
                        decoration.rotate(0, FastMath.PI, 0);
                    }
                    case 2 -> {
                        decoration.setLocalTranslation(px+2, 8.7f, pz+7.2f);
                        decoration.rotate(0, FastMath.PI, 0);
                    }
                    case 3 -> {
                        decoration.setLocalTranslation(px-7.2f, 8.7f, pz+2);
                        decoration.rotate(0, -FastMath.PI, 0);
                    }
                    default -> {
                        decoration.setLocalTranslation(px-2, 8.7f, pz-7.2f);
                        decoration.rotate(0, -FastMath.PI, 0);
                    }
                }
                decoration.scale(10);
            }
            case 15 -> {
                decoration = assetManager.loadModel("Models/Decorations/lounging_mermaid.glb");
                decoration.setLocalTranslation(px, 0.3f, pz);
                switch ((int) direction) {
                    case 1 -> decoration.setLocalTranslation(px-1.3f, -0.08f, pz-9);
                    case 2 -> decoration.setLocalTranslation(px+9, -0.08f, pz-1.3f);
                    case 3 -> decoration.setLocalTranslation(px+1.3f, -0.08f, pz+9);
                    default -> decoration.setLocalTranslation(px-9, -0.08f, pz+1.3f);
                }
                decoration.rotate(0, FastMath.PI, 0);
                decoration.scale(11);
            }
            case 16 -> {
                decoration = assetManager.loadModel("Models/Decorations/gargoyle.glb");
                decoration.rotate(0, FastMath.PI, 0);
                decoration.setLocalTranslation(px, -0.001f, pz);
                decoration.scale(20);
            }
        }

        switch ((int) direction) {
            case 1 -> decoration.rotate(0, -FastMath.HALF_PI, 0);
            case 2 -> decoration.rotate(0, FastMath.PI, 0);
            case 3 -> decoration.rotate(0, FastMath.HALF_PI, 0);
        }

        decoration.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        decoration.setShadowMode(RenderQueue.ShadowMode.Receive);
        localRootNode.attachChild(decoration);

    }
}