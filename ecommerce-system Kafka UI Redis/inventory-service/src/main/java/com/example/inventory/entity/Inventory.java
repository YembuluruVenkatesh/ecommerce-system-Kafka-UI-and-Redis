package com.example.inventory.entity;
import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory implements Serializable {

    @Id
    private String product;

    private Integer stock;
}