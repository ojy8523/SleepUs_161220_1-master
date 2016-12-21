package com.example.seungkyu.dreamtodream;

/**
 * Created by junyepoh on 2016. 12. 14..
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

//import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;

/**
 * Created by seungkyu on 2016-12-05.
 */

public class BoardListViewAdapter extends BaseAdapter {
    private ArrayList<BoardVO> boardList = new ArrayList<BoardVO>() ;
    @Override
    public int getCount() {
        return boardList.size();
    }

    @Override
    public Object getItem(int position) {
        return boardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.board_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView titleTV = (TextView)convertView.findViewById(R.id.titleTV);
        TextView writerTV = (TextView) convertView.findViewById(R.id.writerTV) ;
        TextView dateTV = (TextView) convertView.findViewById(R.id.dateTV) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        BoardVO boardVO = boardList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        titleTV.setText(boardVO.getTitle());
        writerTV.setText(boardVO.getName());
        dateTV.setText(boardVO.getDate());

        return convertView;
    }

    public void addItem(int id, String title, String name, String date) { // DB에서 가져올 때 게시물 pk 값도 가져와야할듯 이걸로 본문 조회해서 본문만 가져오고 나머지는 인텐트.
        BoardVO boardVO = new BoardVO(id, title, name, date);
        boardList.add(boardVO);
    }
}
