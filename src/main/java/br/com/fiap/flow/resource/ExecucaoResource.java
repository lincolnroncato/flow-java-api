package br.com.fiap.flow.resource;

import br.com.fiap.flow.bo.ExecucaoBO;
import br.com.fiap.flow.exception.BusinessException;
import br.com.fiap.flow.to.ExecucaoTO;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/execucoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExecucaoResource {

    private ExecucaoBO execucaoBO;

    public ExecucaoResource() {
        execucaoBO = new ExecucaoBO();
    }

    @GET
    public Response findAll() {
        List<ExecucaoTO> execucoes = execucaoBO.findAll();
        return Response.ok(execucoes).build();  // lista vazia = 200 OK
    }

    @GET
    @Path("/{codigo}")
    public Response findByCodigo(@PathParam("codigo") Long codigo) {
        ExecucaoTO execucao = execucaoBO.findByCodigo(codigo);

        if (execucao == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Execução não encontrada.")
                    .build();
        }

        return Response.ok(execucao).build();
    }

    @GET
    @Path("/usuario/{codUsuario}")
    public Response findByUsuario(@PathParam("codUsuario") Long codUsuario) {
        List<ExecucaoTO> execucoes = execucaoBO.findByUsuario(codUsuario);
        return Response.ok(execucoes).build();   // nunca retornar 404 aqui
    }

    @POST
    public Response save(ExecucaoTO execucao) {
        try {
            ExecucaoTO execucaoSalva = execucaoBO.save(execucao);
            return Response.status(Response.Status.CREATED).entity(execucaoSalva).build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao salvar execução.")
                    .build();
        }
    }

    @PUT
    @Path("/{codigo}")
    public Response update(@PathParam("codigo") Long codigo, ExecucaoTO execucao) {
        try {
            execucao.setCodigo(codigo);

            ExecucaoTO execucaoAtualizada = execucaoBO.update(execucao);
            if (execucaoAtualizada == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Execução não encontrada para atualização.")
                        .build();
            }

            return Response.ok(execucaoAtualizada).build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao atualizar execução.")
                    .build();
        }
    }

    @DELETE
    @Path("/{codigo}")
    public Response delete(@PathParam("codigo") Long codigo) {
        boolean deletado = execucaoBO.delete(codigo);

        if (!deletado) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Execução não encontrada para exclusão.")
                    .build();
        }

        return Response.noContent().build();
    }

    @POST
    @Path("/iniciar")
    public Response iniciar(ExecucaoTO execucao) {
        try {
            if (execucao.getCodProcesso() == null || execucao.getCodUsuario() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Código do processo e código do usuário são obrigatórios.")
                        .build();
            }

            ExecucaoTO execucaoIniciada = execucaoBO.iniciar(execucao.getCodProcesso(), execucao.getCodUsuario());
            return Response.status(Response.Status.CREATED).entity(execucaoIniciada).build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao iniciar execução.")
                    .build();
        }
    }

    @GET
    @Path("/processo/{codProcesso}/usuario/{codUsuario}")
    public Response findByProcessoAndUsuario(@PathParam("codProcesso") Long codProcesso,
                                             @PathParam("codUsuario") Long codUsuario) {
        try {
            ExecucaoTO execucao = execucaoBO.findByProcessoAndUsuario(codProcesso, codUsuario);

            if (execucao == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nenhuma execução encontrada para este processo e usuário.")
                        .build();
            }

            return Response.ok(execucao).build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao buscar execução.")
                    .build();
        }
    }

    @PUT
    @Path("/{codExecucao}/etapas/{codEtapa}/concluir")
    public Response concluirEtapa(@PathParam("codExecucao") Long codExecucao,
                                  @PathParam("codEtapa") Long codEtapa) {
        try {
            ExecucaoTO execucaoAtualizada = execucaoBO.concluirEtapa(codExecucao, codEtapa);
            return Response.ok(execucaoAtualizada).build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao concluir etapa.")
                    .build();
        }
    }

    @PUT
    @Path("/{codExecucao}/finalizar")
    public Response finalizar(@PathParam("codExecucao") Long codExecucao) {
        try {
            ExecucaoTO execucaoFinalizada = execucaoBO.finalizar(codExecucao);
            return Response.ok(execucaoFinalizada).build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao finalizar execução.")
                    .build();
        }
    }
}
