package com.mrjodev.jorecyclermanager;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by josong on 2017-09-18.
 */

public class QuickReturnViewInitializor {
    public static void init(final ScrollView sv, final View vQuickHeader){
        sv.setOnTouchListener(new View.OnTouchListener() {
            boolean onDown = false;
            boolean shownQuickView = true;
            float firstDownY = -1f;
            //위아래 구분용
            float prevY = 0f;
            float currentFilteredY = 0f;
            boolean goUp = false;
            boolean prevGoUp = false;

            @Override
            public boolean onTouch(View v, MotionEvent e) {
                switch(e.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        onDown = true;
                        firstDownY = -1f;
                        prevY = e.getY();
                        currentFilteredY = e.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        //방향 확인용
                        if(Math.abs(prevY-e.getY()) > 1){
                            currentFilteredY = e.getY();
                        }

                        if(prevY < currentFilteredY){
                            //내려감
                            goUp = false;
                        }else{
                            //올라감
                            goUp = true;
                        }

                        //처리
                        if(onDown == true){

                            if(firstDownY == -1f){
                                firstDownY = e.getY();
                            }

                            //안보여지고 있는 상태일때.
                            if(shownQuickView == false ){
                                if(e.getY() - firstDownY > 20){
                                    //보여주기
                                    shownQuickView = true;
                                    showQuickView();
                                }

                                if(goUp){
                                    firstDownY = -1f;
                                }
                            }

                            if(shownQuickView == true){
                                if(e.getY() - firstDownY < -20){
                                    //들어가기
                                    shownQuickView = false;
                                    hideQuickView();
                                }

                                if(!goUp){
                                    firstDownY = -1f;
                                }
                            }
                        }

                        prevGoUp = goUp;
                        prevY = currentFilteredY;
                        break;

                    case MotionEvent.ACTION_UP:
                        firstDownY = -1f;
                        break;
                }

                return false;
            }

            private void showQuickView(){
                vQuickHeader.animate().translationY(0).setDuration(270).start();
            }

            private void hideQuickView(){
                vQuickHeader.animate().translationY(vQuickHeader.getMeasuredHeight()).setDuration(270).start();
            }
        });

    }

    public static void init(final RecyclerView rcv, final View vQuickHeader){
        rcv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            boolean onDown = false;
            boolean shownQuickView = true;
            float firstDownY = -1f;
            //위아래 구분용
            float prevY = 0f;
            float currentFilteredY = 0f;
            boolean goUp = false;
            boolean prevGoUp = false;
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                switch(e.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        onDown = true;
                        firstDownY = -1f;
                        prevY = e.getY();
                        currentFilteredY = e.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        //방향 확인용
                        if(Math.abs(prevY-e.getY()) > 1){
                            currentFilteredY = e.getY();
                        }

                        if(prevY < currentFilteredY){
                            //내려감
                            goUp = false;
                        }else{
                            //올라감
                            goUp = true;
                        }

                        //처리
                        if(onDown == true){

                            if(firstDownY == -1f){
                                firstDownY = e.getY();
                            }

                            //안보여지고 있는 상태일때.
                            if(shownQuickView == false ){
                                if(e.getY() - firstDownY > 20){
                                    //보여주기
                                    shownQuickView = true;
                                    showQuickView();
                                }

                                if(goUp){
                                    firstDownY = -1f;
                                }
                            }

                            if(shownQuickView == true){
                                if(e.getY() - firstDownY < -20){
                                    //들어가기
                                    shownQuickView = false;
                                    hideQuickView();
                                }

                                if(!goUp){
                                    firstDownY = -1f;
                                }
                            }
                        }

                        prevGoUp = goUp;
                        prevY = currentFilteredY;
                        break;

                    case MotionEvent.ACTION_UP:
                        firstDownY = -1f;
                        break;
                }

                return false;
            }

            private void showQuickView(){
                vQuickHeader.animate().translationY(0).setDuration(270).start();
            }

            private void hideQuickView(){
                vQuickHeader.animate().translationY(-vQuickHeader.getMeasuredHeight()).setDuration(270).start();
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }
}
