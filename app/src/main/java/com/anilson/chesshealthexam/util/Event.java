package com.anilson.chesshealthexam.util;

public class Event<T> {

    private T content;

    public Event(T content) {
        this.content = content;
    }

    private boolean hasBeenHandled = false;

    public boolean hasBeenHandled() {
        return hasBeenHandled;
    }

    public T getContentIfNotHandledOrReturnNull() {
        if (hasBeenHandled) {
            return null;
        } else {
            hasBeenHandled = true;
            return content;
        }
    }

    public T peekContent() {
        return content;
    }
}
