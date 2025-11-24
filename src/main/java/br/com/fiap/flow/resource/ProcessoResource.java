package br.com.fiap.flow.resource;

import br.com.fiap.flow.bo.ProcessoBO;
import br.com.fiap.flow.exception.BusinessException;
import br.com.fiap.flow.to.ProcessoTO;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/processos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProcessoResource {

    private ProcessoBO processoBO;

    public ProcessoResource() {
        processoBO = new ProcessoBO();
    }

    @GET
    public Response findAll() {
        List<ProcessoTO> processos = processoBO.findAll();

        // Lista vazia deve retornar 200 OK com []
        return Response.ok(processos).build();
    }

    @GET
    @Path("/{codigo}")
    public Response findByCodigo(@PathParam("codigo") Long codigo) {
        ProcessoTO processo = processoBO.findByCodigo(codigo);

        if (processo == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Processo não encontrado.")
                    .build();
        }

        return Response.ok(processo).build();
    }

    @POST
    public Response save(ProcessoTO processo) {
        try {
            ProcessoTO processoSalvo = processoBO.save(processo);
            return Response.status(Response.Status.CREATED).entity(processoSalvo).build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao salvar processo.").build();
        }
    }

    @PUT
    @Path("/{codigo}")
    public Response update(@PathParam("codigo") Long codigo, ProcessoTO processo) {
        try {
            processo.setCodigo(codigo);
            ProcessoTO processoAtualizado = processoBO.update(processo);

            if (processoAtualizado == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Processo não encontrado para atualização.")
                        .build();
            }

            return Response.ok(processoAtualizado).build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao atualizar processo.").build();
        }
    }

    @DELETE
    @Path("/{codigo}")
    public Response delete(@PathParam("codigo") Long codigo) {
        boolean deletado = processoBO.delete(codigo);

        if (!deletado) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Processo não encontrado para exclusão.")
                    .build();
        }

        return Response.noContent().build();
    }
}
