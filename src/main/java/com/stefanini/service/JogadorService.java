package com.stefanini.service;

import com.stefanini.entity.Jogador;
import com.stefanini.exceptions.RegraDeNegocioException;
import com.stefanini.repository.JogadorRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ValidationException;
import javax.ws.rs.core.Response;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class JogadorService {
	@Inject
	JogadorRepository jogadorRepository;

	public Jogador salvar(Jogador jogador) {
		if (!isSenhaValida(jogador.getPassword())) {
			throw new ValidationException("Senha inválida");
		}
		cifrarSenha(jogador);

		jogadorRepository.save(jogador);

		return jogador;

	}

	private boolean isSenhaValida(String senha) {
		if (senha == null || senha.isBlank()) {
			return false;
		}

		if (senha.length() < 4 || senha.length() > 10) {
			return false;
		}

		return true;
	}

	private void cifrarSenha(Jogador jogador) {

		String senha = jogador.getPassword();

		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			byte messageDigest[];
			messageDigest = algorithm.digest(senha.getBytes("UTF-8"));
			String bsa64 = Base64.getEncoder().encodeToString(messageDigest);
			jogador.setPassword(bsa64);

		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public Jogador pegarPorId(Long id) {
		var jogador = jogadorRepository.findById(id);
		if (Objects.isNull(jogador)) {
			throw new RegraDeNegocioException("Ocorreu um erro ao buscar o Jogador de id " + id,
					Response.Status.NOT_FOUND);
		}
		return jogador;
	}

	public void alterar(Jogador jogador) {
		jogadorRepository.update(jogador);
	}

	public void deletar(Long id) {
		Jogador toDelete = jogadorRepository.findById(id);
		if (toDelete == null) {
			throw new ValidationException("Usuário Não Encontrado");
		}
		jogadorRepository.delete(id);
	}

	public List<Jogador> listarTodos() {
		return jogadorRepository.listAll();
	}
}
