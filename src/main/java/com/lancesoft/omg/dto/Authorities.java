package com.lancesoft.omg.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor(staticName="build")
@NoArgsConstructor
@Entity
public class Authorities {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer role_id;
	private String role;
}
