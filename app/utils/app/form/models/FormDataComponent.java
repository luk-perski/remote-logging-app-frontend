package utils.app.form.models;

import java.util.ArrayList;
import java.util.List;

import models.db.app.form.FormElement;

public class FormDataComponent {

	private String component;

	private String id;

	private List<FormDataComponent> children;

	public FormDataComponent(FormElement element) {
		this.component = element.getComponent();
		this.id = element.getElementID();
		this.children = new ArrayList<FormDataComponent>();

		for (FormElement child : element.getChildren()) {
			FormDataComponent component_instance = FormDataComponent.getComponentInstance(child);
			if (component_instance != null) {
				this.children.add(component_instance);
			}
		}
	}

	public static FormDataComponent getComponentInstance(FormElement element) {
		if (element != null) {
			switch (element.getComponent()) {
			case FormComponent.COMPONENT:
				return new FormComponent(element);
			case RowComponent.COMPONENT:
				return new RowComponent(element);
			case ColComponent.COMPONENT:
				return new ColComponent(element);
			case InputTextComponent.COMPONENT:
				return new InputTextComponent(element);
			case InputPasswordComponent.COMPONENT:
				return new InputPasswordComponent(element);
			case SelectComponent.COMPONENT:
				return new SelectComponent(element);
			case OptionComponent.COMPONENT:
				return new OptionComponent(element);
			case ButtonComponent.COMPONENT:
				return new ButtonComponent(element);
			case FontAwesomeComponent.COMPONENT:
				return new FontAwesomeComponent(element);
			case TextComponent.COMPONENT:
				return new TextComponent(element);
			}
		}

		return null;
	}

	public String getComponent() {
		return this.component;
	}

	public String getID() {
		return this.id;
	}

	public List<FormDataComponent> getChildren() {
		return this.children;
	}
}
