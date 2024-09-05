package br.edu.univille.poo.jpa.service;

import br.edu.univille.poo.jpa.entity.Tarefa;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.univille.poo.jpa.repository.TarefaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
public class TarefaService {
    
    @Autowired
    private TarefaRepository tarefaRepository;

    public List<Tarefa> obterTodos(){
        return tarefaRepository.findAll();
    }
    public Optional<Tarefa> obterPeloId(Long id){
        return tarefaRepository.findById(id);
    }
    public Optional<Tarefa> obterNaoFinalizadas(){
        return tarefaRepository.findAllByFinalizada(false);
    }
    public Optional<Tarefa> obterFinalizadas(){
        return tarefaRepository.findAllByFinalizada(true);
    }
    public Optional<Tarefa> obterNaoFinalizadasEntre(Date inicio, Date fim){
        return tarefaRepository.findAllByFinalizadaAndDtPrevisaoBetween(false, inicio, fim);
    }
    public Optional<Tarefa> obterAtrasadas(){
        LocalDate hoje = LocalDate.now();
        return tarefaRepository.findAllByFinalizadaAndDtPrevisaoBefore(false, hoje);
    }

    public Tarefa incluir(Tarefa tarefa){
        tarefa.setId(Long.valueOf(0));
        if (Strings.isBlank(tarefa.getTitulo())){
            throw new RuntimeException("Titulo Não Informado");
        }
        if (tarefa.getTitulo().length() < 5){
            throw new RuntimeException("O titulo deve conter pelo menos 5 caracteres");
        }
        if (tarefa.getDtPrevisao() == null){
            throw new RuntimeException("Data de Previsão não informada");
        }
        tarefa = tarefaRepository.save(tarefa);
        return tarefa;
    }
    public Tarefa atualizar (Tarefa tarefa){
        Tarefa antigo = tarefaRepository.findById(tarefa.getId()).orElse(null);
        if (antigo == null) throw new RuntimeException("Tarefa Não Encontrada");

        if (antigo.isFinalizada()) throw new RuntimeException("Tarefa já finalizada, não pode ser modificada");

        antigo.setTitulo(tarefa.getTitulo());
        antigo.setDescricao(tarefa.getDescricao());
        antigo.setFinalizada(tarefa.isFinalizada());
        antigo.setDtPrevisao(tarefa.getDtPrevisao());
        antigo.setDtFinalizacao(tarefa.getDtFinalizacao());

        if (Strings.isBlank(tarefa.getTitulo())){
            throw new RuntimeException("Titulo Não Informado");
        }
        if (tarefa.getTitulo().length() < 5){
            throw new RuntimeException("O titulo deve conter pelo menos 5 caracteres");
        }
        if (tarefa.getDtPrevisao() == null){
            throw new RuntimeException("Data de Previsão não informada");
        }
        return tarefaRepository.save(antigo);
    }

    public void deletarPeloId(Tarefa tarefa){
        Optional<Tarefa> tarefaDel = tarefaRepository.findById(tarefa.getId());

        if (tarefaDel.isEmpty()) throw new RuntimeException("Tarefa Não Encontrada");
        if (tarefaDel.get().isFinalizada()) throw new RuntimeException("Tarefa já finalizada, não pode ser excluida");

        tarefaRepository.delete(tarefaDel.get());
    }

    public Tarefa finalizarTarefa(Tarefa tarefa) {
        Optional<Tarefa> taf = tarefaRepository.findById(tarefa.getId());

        if (taf.isEmpty()) throw new RuntimeException("Tarefa Não Encontrada");
        if (taf.get().isFinalizada()) throw new RuntimeException("Tarefa já finalizada, não pode ser excluida");

        taf.get().setFinalizada(true);
        return taf.orElse(null);
    }

}
