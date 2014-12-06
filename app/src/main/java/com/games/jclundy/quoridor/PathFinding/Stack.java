package com.games.jclundy.quoridor.PathFinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devfloater65 on 12/4/14.
 */

public class Stack {
    private int count;
    private List<Integer> stack;
    Stack(){
        count = 0;
        stack = new ArrayList<Integer>();
    }
    void push(Integer move){
        stack.add(move);
        count++;
    }
    void pop(){
        if(stack.size() > 0){
            stack.remove(count - 1);
            count--;
        }
    }

    int peek(){
        return stack.get(count - 1);
    }

    int size(){
        return stack.size();
    }
}