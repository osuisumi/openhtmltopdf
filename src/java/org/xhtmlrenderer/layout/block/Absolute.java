package org.xhtmlrenderer.layout.block;

import org.xhtmlrenderer.layout.BlockFormattingContext;
import org.xhtmlrenderer.layout.Context;
import org.xhtmlrenderer.layout.LayoutUtil;
import org.xhtmlrenderer.render.Box;

public class Absolute {

    public static void preChildrenLayout(Context c, Box block) {
        if (isAbsolute(c, block)) {
            BlockFormattingContext bfc = new BlockFormattingContext(block);
            bfc.setWidth(block.width);
            c.pushBFC(bfc);
        }
    }

    public static void postChildrenLayout(Context c, Box block) {
        if (isAbsolute(c, block)) {
            c.getBlockFormattingContext().doFinalAdjustments();
            c.popBFC();
        }
    }

    private static boolean isAbsolute(Context c, Box box) {
        String position = LayoutUtil.getPosition(c, box);
        if (position.equals("absolute")) {
            return true;
        }
        return false;
    }

    public static void setupAbsolute(Context c, Box box) {
        String position = LayoutUtil.getPosition(c, box);
        if (position.equals("absolute")) {
            if (c.css.getStyle(box.getNode()).hasProperty("right")) {
                //u.p("prop = " + c.css.getProperty(box.getRealElement(),"right",false));
                if (LayoutUtil.hasIdent(c, box.getRealElement(), "right", false)) {
                    if (c.css.getStyle(box.getNode()).getStringProperty("right").equals("auto")) {
                        box.right_set = false;
                        //u.p("right set to auto");
                    }
                } else {
                    box.right = (int) c.css.getStyle(box.getNode()).getFloatPropertyRelative("right", 0);
                    box.right_set = true;
                    //u.p("right set to : " + box.right);
                }
            }
            if (c.css.getStyle(box.getNode()).hasProperty("left")) {
                //u.p("prop = " + c.css.getProperty(box.getRealElement(),"left",false));
                if (LayoutUtil.hasIdent(c, box.getRealElement(), "left", false)) {
                    if (c.css.getStyle(box.getNode()).getStringProperty("left").equals("auto")) {
                        box.left_set = false;
                        //u.p("left set to auto");
                    }
                } else {
                    box.left = (int) c.css.getStyle(box.getNode()).getFloatPropertyRelative("left", 0);
                    box.left_set = true;
                    //u.p("left set to : " + box.left);
                }
            }
            /*
            if ( c.css.hasProperty( box.node, "left", false ) ) {
                box.left = (int)c.css.getFloatProperty( box.node, "left", 0, false );
                box.left_set = true;
            }
            */
            
            if (c.css.getStyle(box.getNode()).hasProperty("bottom")) {
                box.top = (int) c.css.getStyle(box.getNode()).getFloatPropertyRelative("bottom", 0);
                box.bottom_set = true;
            }
            if (c.css.getStyle(box.getNode()).hasProperty("top")) {
                box.top = (int) c.css.getStyle(box.getNode()).getFloatPropertyRelative("top", 0);
                box.top_set = true;
            }
            box.setAbsolute(true);
            
            // if right and left are set calculate width
            if (box.right_set && box.left_set) {
                box.width = box.width - box.right - box.left;
            }
        }
    }

    public static void positionAbsoluteChild(Context c, Box child_box) {
        BlockFormattingContext bfc = c.getBlockFormattingContext();
        // handle the left and right
        if (child_box.right_set) {
            child_box.x = -bfc.getX() + bfc.getWidth() - child_box.right - child_box.width
                    - bfc.getMaster().totalRightPadding();
        } else {
            child_box.x = bfc.getX() + child_box.left;
        }

        // handle the top and bottom
        if (child_box.bottom_set) {
            // can't actually do this part yet, so save for later
            bfc.addAbsoluteBottomBox(child_box);
            /*
            // bottom positioning
            child_box.y = bfc.getY() + bfc.getHeight() - child_box.height - child_box.top + 50;
            u.p("bfc = " + bfc);
            u.p("bfc.height = " + bfc.getHeight());
            u.p("final child box = " + child_box);
            */
        } else {
            // top positioning
            child_box.y = bfc.getY() + child_box.top;
        }
    }

}