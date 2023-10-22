package com.example.androidapp;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

public class PlayerSettings {
    public int increment =0;
    public int cooldown =0;
    public PlayerSettings(int increment, int cooldown){setIncrement(increment); setCooldown(cooldown);}
    public void setIncrement(int time){ increment = time; }
    public void setCooldown(int cd) {cooldown = cd;}

    @Override
    public String toString() {
        return "Increment: " + increment +"\nCooldown: "+ cooldown;
    }
}
