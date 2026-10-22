Migrations appliquées au projet (dans l'ordre) :
1. balal_initial_schema
2. balal_private_phone
3. durcissement manuel : policy profiles_select_all, vue public_profiles en security_invoker,
   droits colonne par colonne sur profiles (phone verrouillé), handle_new_user / is_admin en invoker.
La colonne profiles.phone est conservée mais vide et illisible ; à supprimer proprement plus tard.
