package com.revature.resproject.dtos.responses;

import com.revature.resproject.models.Role;

public class Principal {
        private int id;
        private String username;
        private Role role;

        public Principal() {
            super();
        }

        public Principal(int id, String username, Role role) {
            this.id = id;
            this.username = username;
            this.role = role;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        @Override
        public String toString() {
            return "Principal{" +
                    "id='" + id + '\'' +
                    ", username='" + username + '\'' +
                    ", role=" + role +
                    '}';
        }
}
