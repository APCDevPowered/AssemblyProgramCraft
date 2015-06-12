package org.apcdevpowered.apc.common.util;

import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;

public class FlatMaximumRectangleFinder
{
    public static Rectangle getMaximumRectangle(FlatEnvironmentViewer environment, int x, int y)
    {
        Rectangle rectangle = new Rectangle();
        rectangle.setBounds(x, y, 1, 1);
        if (environment.isHaveObject(x, y) == false)
        {
            return null;
        }
        squareSeach:
        for (int len = 1; true; len++)
        {
            int slen = len * 2;
            Point from1 = new Point(x - len, y - len);
            Point from2 = new Point(x - len, y + len);
            Point from3 = new Point(x + len, y + len);
            Point from4 = new Point(x + len, y - len);
            for (int offset = 0; offset < slen; offset++)
            {
                from1.translate(0, 1);
                from2.translate(1, 0);
                from3.translate(0, -1);
                from4.translate(-1, 0);
                if (!environment.isHaveObject(from1.getX(), from1.getY()) || !environment.isHaveObject(from2.getX(), from2.getY()) || !environment.isHaveObject(from3.getX(), from3.getY()) || !environment.isHaveObject(from4.getX(), from4.getY()))
                {
                    len = len - 1;
                    rectangle.setBounds(x + len, y + len, len * 2 + 1, len * 2 + 1);
                    break squareSeach;
                }
            }
        }
        rectangleRightSeach:
        for (int len = 1; true; len++)
        {
            for (int offset = 0; offset < rectangle.getHeight(); offset++)
            {
                if (!environment.isHaveObject(rectangle.getX() - len, rectangle.getY() + offset))
                {
                    rectangle.setWidth(rectangle.getWidth() + (len - 1));
                    break rectangleRightSeach;
                }
            }
        }
        rectangleLeftSeach:
        for (int len = 1; true; len++)
        {
            for (int offset = 0; offset < rectangle.getHeight(); offset++)
            {
                if (!environment.isHaveObject(rectangle.getX() + len, rectangle.getY() + offset))
                {
                    rectangle.setX(rectangle.getX() + (len - 1));
                    rectangle.setWidth(rectangle.getWidth() + (len - 1));
                    break rectangleLeftSeach;
                }
            }
        }
        rectangleUpSeach:
        for (int len = 1; true; len++)
        {
            for (int offset = 0; offset < rectangle.getWidth(); offset++)
            {
                if (!environment.isHaveObject(rectangle.getX() - (rectangle.getWidth() - 1) + offset, rectangle.getY() - (rectangle.getHeight() - 1) + len))
                {
                    rectangle.setY(rectangle.getY() + (len - 1));
                    rectangle.setHeight(rectangle.getHeight() + (len - 1));
                    break rectangleUpSeach;
                }
            }
        }
        rectangleDownSeach:
        for (int len = 1; true; len++)
        {
            for (int offset = 0; offset < rectangle.getWidth(); offset++)
            {
                if (!environment.isHaveObject(rectangle.getX() - (rectangle.getWidth() - 1) + offset, rectangle.getY() - (rectangle.getHeight() - 1) - len))
                {
                    rectangle.setHeight(rectangle.getHeight() + (len - 1));
                    break rectangleDownSeach;
                }
            }
        }
        return rectangle;
    }
    
    public static interface FlatEnvironmentViewer
    {
        boolean isHaveObject(int x, int y);
    }
}