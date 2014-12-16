Deck.____Shuffle:
	BeginFunc 4 ;
	_tmp0 = "Shuffle" ;
	PushParam _tmp0 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
VTable Deck =
	Deck.____Shuffle,
; 
Player.____Init:
	BeginFunc 0 ;
	*(this + 4) = dj ;
	EndFunc ;
Player.____GetDeck:
	BeginFunc 4 ;
	_tmp1 = *(this + 4) ;
	Return _tmp1 ;
	EndFunc ;
VTable Player =
	Player.____Init,
	Player.____GetDeck,
; 
main:
	BeginFunc 80 ;
	_tmp2 = 4 ;
	_tmp3 = 4 ;
	_tmp4 = _tmp3 + _tmp2 ;
	PushParam _tmp4 ;
	_tmp5 = LCall _Alloc ;
	PopParams 4 ;
	_tmp6 = Player ;
	*(_tmp5) = _tmp6 ;
	p = _tmp5 ;
	_tmp7 = 0 ;
	_tmp8 = 4 ;
	_tmp9 = _tmp8 + _tmp7 ;
	PushParam _tmp9 ;
	_tmp10 = LCall _Alloc ;
	PopParams 4 ;
	_tmp11 = Deck ;
	*(_tmp10) = _tmp11 ;
	PushParam _tmp10 ;
	PushParam p ;
	_tmp12 = *(p) ;
	_tmp13 = *(_tmp12) ;
	ACall _tmp13 ;
	PopParams 8 ;
	PushParam p ;
	_tmp14 = *(p) ;
	_tmp15 = *(_tmp14 + 4) ;
	_tmp16 = ACall _tmp15 ;
	PopParams 4 ;
	PushParam _tmp16 ;
	_tmp17 = *(_tmp16) ;
	_tmp18 = *(_tmp17) ;
	ACall _tmp18 ;
	PopParams 4 ;
	EndFunc ;
