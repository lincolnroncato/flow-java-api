package br.com.fiap.flow.resource;

import br.com.fiap.flow.bo.UsuarioBO;
import br.com.fiap.flow.exception.BusinessException;
import br.com.fiap.flow.to.UsuarioTO;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    private UsuarioBO usuarioBO;

    public UsuarioResource() {
        usuarioBO = new UsuarioBO();
    }

    @GET
    public Response findAll() {
        List<UsuarioTO> usuarios = usuarioBO.findAll();
        return Response.ok(usuarios).build(); // lista vazia = 200 OK
    }

    @GET
    @Path("/{codigo}")
    public Response findByCodigo(@PathParam("codigo") Long codigo) {
        UsuarioTO usuario = usuarioBO.findByCodigo(codigo);

        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuário não encontrado.")
                    .build();
        }

        return Response.ok(usuario).build();
    }

    @POST
    public Response save(UsuarioTO usuario) {
        try {
            UsuarioTO usuarioSalvo = usuarioBO.save(usuario);
            return Response.status(Response.Status.CREATED).entity(usuarioSalvo).build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao salvar usuário.")
                    .build();
        }
    }

    @PUT
    @Path("/{codigo}")
    public Response update(@PathParam("codigo") Long codigo, UsuarioTO usuario) {
        try {
            usuario.setCodigo(codigo);
            UsuarioTO usuarioAtualizado = usuarioBO.update(usuario);

            if (usuarioAtualizado == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Usuário não encontrado para atualização.")
                        .build();
            }

            return Response.ok(usuarioAtualizado).build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao atualizar usuário.")
                    .build();
        }
    }

    @DELETE
    @Path("/{codigo}")
    public Response delete(@PathParam("codigo") Long codigo) {
        try {
            boolean deletado = usuarioBO.delete(codigo);

            if (!deletado) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Usuário não encontrado para exclusão.")
                        .build();
            }

            return Response.noContent().build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
