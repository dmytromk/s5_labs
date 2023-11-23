package com.dmytromk.asteroids.common;

public class Vector2 {
    public float x;
    public float y;


    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 orthogonal() {
        return new Vector2(-this.y, this.x);
    }

    public static Vector2 add(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.x + v2.x, v1.y + v2.y);
    }

    public static Vector2 subtract(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.x - v2.x, v1.y - v2.y);
    }

    public static Vector2 multiply(Vector2 vector, int scalar) {
        return new Vector2(vector.x * scalar, vector.y * scalar);
    }

    public static double length(Vector2 vector) {
        return Math.sqrt(vector.x * vector.x + vector.y * vector.y);
    }

    public static float dotProduct(Vector2 v1, Vector2 v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    public static double distance(Vector2 v1, Vector2 v2) {
        float dx = v1.x - v2.x;
        float dy = v1.y - v2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
