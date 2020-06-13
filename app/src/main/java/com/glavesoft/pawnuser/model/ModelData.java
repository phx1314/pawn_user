package com.glavesoft.pawnuser.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2018/1/9.
 */

public class ModelData<T> implements Serializable {
    public List<T> mList = new ArrayList();

}
