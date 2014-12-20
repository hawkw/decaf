main:
	BeginFunc 24 ;
	_tmp0 = 1 ;
	_tmp1 = 0 ;
	_tmp2 = _tmp0 && _tmp1 ;
	PushParam _tmp2 ;
	LCall _PrintBool ;
	PopParams 4 ;
	_tmp3 = 1 ;
	_tmp4 = 0 ;
	_tmp5 = _tmp3 || _tmp4 ;
	PushParam _tmp5 ;
	LCall _PrintBool ;
	PopParams 4 ;
	EndFunc ;
