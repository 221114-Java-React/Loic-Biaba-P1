package com.revature.resproject.models;

public enum Role {
     ADMIN(0), MANAGER(1), DEFAULT(2);
     private int i;
     Role(int i) {
          this.i = i;
     }
}
