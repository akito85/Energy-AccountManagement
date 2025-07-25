/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.common.base.utils;

/**
 *
 * @author RachmatY
 */
public class EmptyStackException extends RuntimeException {

    private static final long serialVersionUID = 6355800580849179587L;

    public EmptyStackException() {
        super();
    }

    public EmptyStackException(String s) {
        super(s);
    }

    public EmptyStackException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public EmptyStackException(Throwable throwable) {
        super(throwable);
    }
    
}
