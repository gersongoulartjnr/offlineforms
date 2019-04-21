package br.unifesp.maritaca.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;

import br.unifesp.maritaca.persistence.EntityManagerFileSystem;
import br.unifesp.maritaca.persistence.EntityManagerHector;

public class AbstractDAO {

	@Autowired
	protected EntityManagerHector emHector;
	
	@Autowired
	protected EntityManagerFileSystem emFileSystem;
}
