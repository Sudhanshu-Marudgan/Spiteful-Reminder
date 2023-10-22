package com.example.spiteful_reminder;

public class helper {
        String Memo,Time,Date;

        public helper(String memo, String time, String date){
            Memo = memo;
            Time = time;
            Date = date;
        }

        public helper(){
        }

        public String getMemo(){
            return Memo;
        }

        public void setMemo(String memo){Memo = memo;}

        public String getTime(){return Time;}

        public void setTime(String time){Time = time;}

        public String getDate(){return Date;}

        public void setDate(String date){Date = date;}
}
