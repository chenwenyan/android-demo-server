package com.nenu.android.bag;

/**
 * test
 * Author： wychen
 * Date: 2017/5/30
 * Time: 10:15
 */
public class Test {

    public static void main(String[] args){


        int[] weight = {3,4,6,2,5};  //每个物品重量
        int[] val = {6,8,7,5,9};    //每个物品价值
        int maxw = 10;   //书包容量
        oneArr(weight,val,maxw);
    }

    public static int oneArr(int[] weight,int[] val,int maxw){
        int[] f = new int[maxw+1];
        for(int i=0;i<f.length;i++){
            f[i] = 0;
        }
        for(int i=0;i<val.length;i++){
            System.out.println("i="+i);
            System.out.println("weight[i]=" + weight[i]);
            for(int j=weight[i];j<f.length;j++){
                f[j] = Math.max(f[j], f[j-weight[i]]+val[i]);
                System.out.println("f[j]=" + f[j]);
            }
        }
        return f[maxw];
    }

//    static int[] twoArr(int[] weight,int[] val,int maxw){
//        int value = new int[][];
//        int result = new int[];
//        for(int i= 0; i <weight.length; i++){
//            value[i][0] = 0;
//        }
//        for(int j = 0; j < maxw; j++){
//            value[0][j] = 0;
//        }
//        for(int i = 0; i< weight.length; i++){
//            for(int j = 0; j <= maxw; j++ ){
//                if(j < weight[i]){
//                    value[i][j] = value[i-1][j];
//                }else{
//                    value[i][j] = Math.max(value[i-1][j-weight[i]]+val[i],value[i-1][j]);
//                }
//
//                j = maxw;
//                for(i = weight.length-1; i >= 0; i--){
//                    if(value[i][j] > value[i-1][j]){
//                        result[i] = 1;
//                        j = j - weight[i];
//                    }else{
//                        result[i] = 0;
//                    }
//                }
//            }
//        }
//        return result;
//    }

}
