BEGIN;

INSERT INTO public.role (role_name) VALUES
('Developer'),
('Manager');

END;
$$ LANGUAGE plpgsql;