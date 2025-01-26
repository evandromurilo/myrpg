package com.evandromurilo.myrpg;

public class StatusModifier {
    private StatusType statusType;
    private float modifier;

    public StatusModifier(StatusType statusType, float modifier) {
        this.statusType = statusType;
        this.modifier = modifier;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public float getModifier() {
        return modifier;
    }
}
