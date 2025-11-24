package br.com.fiap.flow.resource;

import br.com.fiap.flow.bo.EtapaBO;
import br.com.fiap.flow.exception.BusinessException;
import br.com.fiap.flow.to.EtapaTO;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/etapas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EtapaResource {

    private EtapaBO etapaBO;

    public EtapaResource() {
        etapaBO = new EtapaBO();
    }

    @GET
    public Response findAll() {
        List<EtapaTO> etapas = etapaBO.findAll();
        // lista vazia -> 200 OK com []
        return Response.ok(etapas).build();
    }

    @GET
    @Path("/{codigo}")
    public Response findByCodigo(@PathParam("codigo") Long codigo) {
        EtapaTO etapa = etapaBO.findByCodigo(codigo);

        if (etapa == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Etapa não encontrada.")
                    .build();
        }

        return Response.ok(etapa).build();
    }

    @GET
    @Path("/processo/{codProcesso}")
    public Response findByProcesso(@PathParam("codProcesso") Long codProcesso) {
        List<EtapaTO> etapas = etapaBO.findByProcesso(codProcesso);
        // mesmo padrão: retorna lista vazia
        return Response.ok(etapas).build();
    }

    @POST
    public Response save(EtapaTO etapa) {
        try {
            EtapaTO etapaSalva = etapaBO.save(etapa);
            return Response.status(Response.Status.CREATED).entity(etapaSalva).build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Erro ao salvar etapa.").build();
        }
    }

    @PUT
    @Path("/{codigo}")
    public Response update(@PathParam("codigo") Long codigo, EtapaTO etapa) {
        try {
            etapa.setCodigo(codigo);
            EtapaTO etapaAtualizada = etapaBO.update(etapa);

            if (etapaAtualizada == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Etapa não encontrada para atualização.")
                        .build();
            }

            return Response.ok(etapaAtualizada).build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Erro ao atualizar etapa.").build();
        }
    }

    @DELETE
    @Path("/{codigo}")
    public Response delete(@PathParam("codigo") Long codigo) {
        boolean deletado = etapaBO.delete(codigo);

        if (!deletado) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Etapa não encontrada para exclusão.")
                    .build();
        }

        return Response.noContent().build();
    }
}
