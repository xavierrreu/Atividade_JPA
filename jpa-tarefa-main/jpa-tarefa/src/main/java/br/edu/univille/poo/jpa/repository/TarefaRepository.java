package br.edu.univille.poo.jpa.repository;

import br.edu.univille.poo.jpa.entity.Tarefa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;


public interface TarefaRepository extends JpaRepository<Tarefa, Long>{

    Optional<Tarefa> findAllByFinalizada(boolean finalizada);
    Optional<Tarefa> findAllByFinalizadaAndDtPrevisaoBefore(boolean finalizado, LocalDate data);
    Optional<Tarefa> findAllByFinalizadaAndDtPrevisaoBetween(boolean finalizado, Date inicio, Date fim);
}
