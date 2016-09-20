package org.protege.editor.owl.ui.action;

import org.protege.editor.core.util.Recommendation;
import org.protege.editor.owl.model.hierarchy.tabbed.CreateHierarchyChangeGenerator;
import org.protege.editor.owl.model.hierarchy.tabbed.MakeSiblingsDisjointChangeGenerator;
import org.protege.editor.owl.ui.hierarchy.creation.CreateHierarchyExecutor;
import org.semanticweb.owlapi.model.*;

import java.awt.event.ActionEvent;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Sep 16
 */
public class CreateSubObjectPropertyHierarchyAction extends SelectedOWLObjectPropertyAction {

    @Override
    protected void initialiseAction() throws Exception {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        OWLObjectProperty rootProperty = getOWLWorkspace().getOWLSelectionModel().getLastSelectedObjectProperty();
        if(rootProperty == null) {
            return;
        }
        OWLDataFactory df = getOWLDataFactory();
        CreateHierarchyChangeGenerator<OWLObjectProperty> hierarchyChangeGenerator = new CreateHierarchyChangeGenerator<>(
                rootProperty,
                getOWLModelManager().getOWLEntityFinder(),
                getOWLModelManager().getOWLEntityFactory(),
                (parent, child) -> df.getOWLSubObjectPropertyOfAxiom(child, parent),
                getOWLModelManager().getActiveOntology()
        );
        MakeSiblingsDisjointChangeGenerator<OWLObjectProperty> disjointsChangeGenerator = new MakeSiblingsDisjointChangeGenerator<>(
                getOWLModelManager().getOWLHierarchyManager().getOWLObjectPropertyHierarchyProvider(),
                (sourceSiblings) -> sourceSiblings,
                (siblings) -> df.getOWLDisjointObjectPropertiesAxiom(siblings),
                getOWLModelManager().getActiveOntology()
        );
        CreateHierarchyExecutor<OWLObjectProperty> executor = new CreateHierarchyExecutor<>(
                getOWLEditorKit(),
                EntityType.OBJECT_PROPERTY,
                Optional.of(Recommendation.NOT_RECOMMENDED),
                hierarchyChangeGenerator,
                disjointsChangeGenerator
        );
        executor.showWizardAndCreateHierarchy();
    }

    public void dispose() {
    }
}
