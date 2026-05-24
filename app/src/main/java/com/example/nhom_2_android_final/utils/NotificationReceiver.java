package com.example.nhom_2_android_final.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra("type");
        if ("STUDY_REMINDER".equals(type)) {
            NotificationHelper.showNotification(context, "Giờ học đến rồi!", "Đã đến lúc ôn tập kiến thức mới rồi bạn ơi. Vào app ngay nào!");
        } else if ("DAILY_TIP".equals(type)) {
            String[] tips = {
                "Hãy chia nhỏ thời gian học để hiệu quả hơn nhé!",
                "Đừng quên uống nước khi học bài.",
                "Học nhóm giúp bạn nhớ kiến thức lâu hơn đấy.",
                "Ôn lại bài ngay sau khi học giúp ghi nhớ tốt hơn.",
                "Ngủ đủ giấc là chìa khóa để có bộ não minh mẫn."
            };
            int randomIndex = (int) (Math.random() * tips.length);
            NotificationHelper.showNotification(context, "Lời khuyên hôm nay", tips[randomIndex]);
        }
    }
}
