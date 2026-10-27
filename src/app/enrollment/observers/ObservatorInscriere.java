package app.enrollment.observers;

import app.enrollment.model.Enrollment;

public interface ObservatorInscriere {

    void laInscriere(Enrollment enrollment);
    void laRenuntare(Enrollment enrollment);

}
