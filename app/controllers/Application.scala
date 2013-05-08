package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Task

object Application extends Controller {
  
  def index = Action {
    Redirect(routes.Application.tasks)
  }

  val taskForm = Form(
	"label" -> nonEmptyText
  )

  val taskUpdateForm = Form(
  	tuple("id" -> nonEmptyText,
  	"label" -> nonEmptyText)
  	)

  def tasks = Action {
	Ok(views.html.index(Task.all(), taskForm, taskUpdateForm))
  }
	
  def newTask = Action {
	implicit request => 
		taskForm.bindFromRequest.fold(
			errors => BadRequest(views.html.index(Task.all(), errors, taskUpdateForm)),
			label => {
				Task.create(label)
				Redirect(routes.Application.tasks)
			}
		)
	}
	
  def update(id: Long) = Action {
  	implicit request =>
  		taskUpdateForm.bindFromRequest.fold(
  			errors => BadRequest(views.html.index(Task.all(), taskForm, errors)),
  			value => {
  				Task.update(value._1.toLong, value._2)
  				Redirect(routes.Application.tasks)
  			  }
  		  )
  }


  def deleteTask(id: Long) = Action {
	Task.delete(id)
	Redirect(routes.Application.tasks)
	}
  
}