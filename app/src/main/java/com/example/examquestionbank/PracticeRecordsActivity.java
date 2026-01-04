package com.example.examquestionbank;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;

/**
 * 查看练习记录界面
 */
public class PracticeRecordsActivity extends AppCompatActivity implements PracticeRecordRecyclerViewAdapter.OnItemClickListener {
    private RecyclerView rvPracticeRecords;
    private TextView tvNoRecords;
    private PracticeRecordManager recordManager;
    private PracticeRecordRecyclerViewAdapter adapter;
    private List<PracticeRecord> records;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_records);
        
        // 初始化视图组件
        initViews();
        
        // 初始化记录管理器
        recordManager = new PracticeRecordManager(this);
        
        // 加载练习记录
        loadPracticeRecords();
        
        // 设置标题
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("练习记录");
        }
    }
    
    private void initViews() {
        rvPracticeRecords = findViewById(R.id.rvPracticeRecords);
        tvNoRecords = findViewById(R.id.tvNoRecords);
    }
    
    private void loadPracticeRecords() {
        // 获取所有练习记录
        records = recordManager.getAllPracticeRecords();
        
        if (records.isEmpty()) {
            // 没有记录时显示提示
            tvNoRecords.setVisibility(View.VISIBLE);
            rvPracticeRecords.setVisibility(View.GONE);
        } else {
            // 有记录时显示列表
            tvNoRecords.setVisibility(View.GONE);
            rvPracticeRecords.setVisibility(View.VISIBLE);
            
            // 创建适配器并设置给RecyclerView
            adapter = new PracticeRecordRecyclerViewAdapter(this, records, this);
            rvPracticeRecords.setAdapter(adapter);
            
            // 添加滑动删除功能
            setupSwipeToDelete();
        }
    }
    
    /**
     * 设置滑动删除功能
     */
    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // 获取被删除的位置
                final int position = viewHolder.getAdapterPosition();
                
                // 显示删除确认对话框
                new AlertDialog.Builder(PracticeRecordsActivity.this)
                        .setTitle("删除记录")
                        .setMessage("确定要删除这条练习记录吗？此操作不可恢复。")
                        .setPositiveButton("确定", (dialog, which) -> {
                            // 执行删除操作
                            deleteRecord(position);
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                            // 取消删除，恢复视图
                            adapter.notifyItemChanged(position);
                        })
                        .setCancelable(false)
                        .show();
            }
            
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // 可以在这里自定义滑动效果
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        
        // 附加ItemTouchHelper到RecyclerView
        new ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(rvPracticeRecords);
    }
    
    /**
     * 删除记录
     */
    private void deleteRecord(int position) {
        // 获取要删除的记录
        PracticeRecord record = adapter.getItem(position);
        
        // 从数据列表中删除
        adapter.removeItem(position);
        
        // 从本地存储中删除
        recordManager.deletePracticeRecord(record.getId());
        
        // 显示删除成功提示
        Toast.makeText(this, "记录已删除", Toast.LENGTH_SHORT).show();
        
        // 如果列表为空，显示空状态
        if (adapter.getItemCount() == 0) {
            tvNoRecords.setVisibility(View.VISIBLE);
            rvPracticeRecords.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onItemClick(int position) {
        // 不再需要点击跳转功能
    }
    
    @Override
    public void onDeleteClick(int position) {
        // 执行删除操作
        deleteRecord(position);
    }
}