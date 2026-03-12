package com.example.emergencycontacthelper;

public class EmergencyContact {

        // ─────────────────────────────────────────────
        // Fields — matches your SQLite table columns
        // ─────────────────────────────────────────────
        private int id;
        private int userId;       // links to logged-in user
        private String name;
        private String phone;
        private String relation;  // e.g. Family, Friend, Medical

        // ─────────────────────────────────────────────
        // Constructor
        // ─────────────────────────────────────────────
        public EmergencyContact(int id, String name, String phone) {
            this.id       = id;
            this.userId   = userId;
            this.name     = name;
            this.phone    = phone;
            this.relation = relation;
        }

        // ─────────────────────────────────────────────
        // Getters
        // ─────────────────────────────────────────────
        public int getId()           { return id; }
        public int getUserId()       { return userId; }
        public String getName()      { return name; }
        public String getPhone()     { return phone; }
        public String getRelation()  { return relation; }

        // ─────────────────────────────────────────────
        // Setters
        // ─────────────────────────────────────────────
        public void setId(int id)              { this.id = id; }
        public void setUserId(int userId)      { this.userId = userId; }
        public void setName(String name)       { this.name = name; }
        public void setPhone(String phone)     { this.phone = phone; }
        public void setRelation(String relation) { this.relation = relation; }
    }
