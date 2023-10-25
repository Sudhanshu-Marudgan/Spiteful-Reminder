package com.example.spiteful_reminder;

public class helper {
        String Memo,Time,Date,Status;

        public helper(String memo, String time, String date,String status){
            Memo = memo;
            Time = time;
            Date = date;
            Status = status;
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

        public String getStatus(){return Status;}

        public void setStatus(String status){Status = status;}
}
