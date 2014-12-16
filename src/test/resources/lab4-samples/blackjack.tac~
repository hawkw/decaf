Random.____Init:
	BeginFunc 0 ;
	*(this + 4) = seedVal ;
	EndFunc ;
Random.____GenRandom:
	BeginFunc 40 ;
	_tmp0 = 15625 ;
	_tmp1 = *(this + 4) ;
	_tmp2 = 10000 ;
	_tmp3 = _tmp1 % _tmp2 ;
	_tmp4 = _tmp0 * _tmp3 ;
	_tmp5 = 22221 ;
	_tmp6 = _tmp4 + _tmp5 ;
	_tmp7 = 65536 ;
	_tmp8 = _tmp6 % _tmp7 ;
	*(this + 4) = _tmp8 ;
	_tmp9 = *(this + 4) ;
	Return _tmp9 ;
	EndFunc ;
Random.____RndInt:
	BeginFunc 16 ;
	PushParam this ;
	_tmp10 = *(this) ;
	_tmp11 = *(_tmp10 + 4) ;
	_tmp12 = ACall _tmp11 ;
	PopParams 4 ;
	_tmp13 = _tmp12 % max ;
	Return _tmp13 ;
	EndFunc ;
VTable Random =
	Random.____Init,
	Random.____GenRandom,
	Random.____RndInt,
; 
Deck.____Init:
	BeginFunc 40 ;
	_tmp14 = 52 ;
	_tmp15 = 4 ;
	_tmp16 = 0 ;
	_tmp17 = _tmp14 < _tmp16 ;
	_tmp18 = _tmp14 == _tmp16 ;
	_tmp19 = _tmp17 || _tmp18 ;
	IfZ _tmp19 Goto _L0 ;
	_tmp20 = "Decaf runtime error: Array size is <= 0\n" ;
	PushParam _tmp20 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L0:
	_tmp21 = _tmp14 * _tmp15 ;
	_tmp22 = _tmp15 + _tmp21 ;
	PushParam _tmp22 ;
	_tmp23 = LCall _Alloc ;
	PopParams 4 ;
	*(_tmp23) = _tmp14 ;
	*(this + 8) = _tmp23 ;
	EndFunc ;
Deck.____Shuffle:
	BeginFunc 400 ;
	_tmp24 = 0 ;
	*(this + 4) = _tmp24 ;
_L1:
	_tmp25 = *(this + 4) ;
	_tmp26 = 52 ;
	_tmp27 = _tmp25 < _tmp26 ;
	IfZ _tmp27 Goto _L2 ;
	_tmp28 = *(this + 4) ;
	_tmp29 = 1 ;
	_tmp30 = _tmp28 + _tmp29 ;
	_tmp31 = 13 ;
	_tmp32 = _tmp30 % _tmp31 ;
	_tmp33 = *(this + 8) ;
	_tmp34 = *(this + 4) ;
	_tmp35 = 0 ;
	_tmp36 = *(_tmp33) ;
	_tmp37 = _tmp34 < _tmp35 ;
	_tmp38 = _tmp36 < _tmp34 ;
	_tmp39 = _tmp36 == _tmp34 ;
	_tmp40 = _tmp38 || _tmp39 ;
	_tmp41 = _tmp40 || _tmp37 ;
	IfZ _tmp41 Goto _L3 ;
	_tmp42 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp42 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L3:
	_tmp43 = 4 ;
	_tmp44 = _tmp34 * _tmp43 ;
	_tmp45 = _tmp44 + _tmp43 ;
	_tmp46 = _tmp33 + _tmp45 ;
	*(_tmp46) = _tmp32 ;
	_tmp47 = *(_tmp46) ;
	_tmp48 = *(this + 4) ;
	_tmp49 = 1 ;
	_tmp50 = _tmp48 + _tmp49 ;
	*(this + 4) = _tmp50 ;
	Goto _L1 ;
_L2:
_L4:
	_tmp51 = 0 ;
	_tmp52 = *(this + 4) ;
	_tmp53 = _tmp51 < _tmp52 ;
	IfZ _tmp53 Goto _L5 ;
	_tmp54 = *(this + 4) ;
	PushParam _tmp54 ;
	PushParam gRnd ;
	_tmp55 = *(gRnd) ;
	_tmp56 = *(_tmp55 + 8) ;
	_tmp57 = ACall _tmp56 ;
	PopParams 8 ;
	r = _tmp57 ;
	_tmp58 = *(this + 4) ;
	_tmp59 = 1 ;
	_tmp60 = _tmp58 - _tmp59 ;
	*(this + 4) = _tmp60 ;
	_tmp61 = *(this + 8) ;
	_tmp62 = *(this + 4) ;
	_tmp63 = 0 ;
	_tmp64 = *(_tmp61) ;
	_tmp65 = _tmp62 < _tmp63 ;
	_tmp66 = _tmp64 < _tmp62 ;
	_tmp67 = _tmp64 == _tmp62 ;
	_tmp68 = _tmp66 || _tmp67 ;
	_tmp69 = _tmp68 || _tmp65 ;
	IfZ _tmp69 Goto _L6 ;
	_tmp70 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp70 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L6:
	_tmp71 = 4 ;
	_tmp72 = _tmp62 * _tmp71 ;
	_tmp73 = _tmp72 + _tmp71 ;
	_tmp74 = _tmp61 + _tmp73 ;
	_tmp75 = *(_tmp74) ;
	temp = _tmp75 ;
	_tmp76 = *(this + 8) ;
	_tmp77 = 0 ;
	_tmp78 = *(_tmp76) ;
	_tmp79 = r < _tmp77 ;
	_tmp80 = _tmp78 < r ;
	_tmp81 = _tmp78 == r ;
	_tmp82 = _tmp80 || _tmp81 ;
	_tmp83 = _tmp82 || _tmp79 ;
	IfZ _tmp83 Goto _L7 ;
	_tmp84 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp84 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L7:
	_tmp85 = 4 ;
	_tmp86 = r * _tmp85 ;
	_tmp87 = _tmp86 + _tmp85 ;
	_tmp88 = _tmp76 + _tmp87 ;
	_tmp89 = *(_tmp88) ;
	_tmp90 = *(this + 8) ;
	_tmp91 = *(this + 4) ;
	_tmp92 = 0 ;
	_tmp93 = *(_tmp90) ;
	_tmp94 = _tmp91 < _tmp92 ;
	_tmp95 = _tmp93 < _tmp91 ;
	_tmp96 = _tmp93 == _tmp91 ;
	_tmp97 = _tmp95 || _tmp96 ;
	_tmp98 = _tmp97 || _tmp94 ;
	IfZ _tmp98 Goto _L8 ;
	_tmp99 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp99 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L8:
	_tmp100 = 4 ;
	_tmp101 = _tmp91 * _tmp100 ;
	_tmp102 = _tmp101 + _tmp100 ;
	_tmp103 = _tmp90 + _tmp102 ;
	*(_tmp103) = _tmp89 ;
	_tmp104 = *(_tmp103) ;
	_tmp105 = *(this + 8) ;
	_tmp106 = 0 ;
	_tmp107 = *(_tmp105) ;
	_tmp108 = r < _tmp106 ;
	_tmp109 = _tmp107 < r ;
	_tmp110 = _tmp107 == r ;
	_tmp111 = _tmp109 || _tmp110 ;
	_tmp112 = _tmp111 || _tmp108 ;
	IfZ _tmp112 Goto _L9 ;
	_tmp113 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp113 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L9:
	_tmp114 = 4 ;
	_tmp115 = r * _tmp114 ;
	_tmp116 = _tmp115 + _tmp114 ;
	_tmp117 = _tmp105 + _tmp116 ;
	*(_tmp117) = temp ;
	_tmp118 = *(_tmp117) ;
	Goto _L4 ;
_L5:
	EndFunc ;
Deck.____GetCard:
	BeginFunc 104 ;
	_tmp119 = 52 ;
	_tmp120 = *(this + 4) ;
	_tmp121 = _tmp119 < _tmp120 ;
	_tmp122 = _tmp119 == _tmp120 ;
	_tmp123 = _tmp121 || _tmp122 ;
	IfZ _tmp123 Goto _L10 ;
	_tmp124 = 0 ;
	Return _tmp124 ;
	Goto _L11 ;
_L10:
_L11:
	_tmp125 = *(this + 8) ;
	_tmp126 = *(this + 4) ;
	_tmp127 = 0 ;
	_tmp128 = *(_tmp125) ;
	_tmp129 = _tmp126 < _tmp127 ;
	_tmp130 = _tmp128 < _tmp126 ;
	_tmp131 = _tmp128 == _tmp126 ;
	_tmp132 = _tmp130 || _tmp131 ;
	_tmp133 = _tmp132 || _tmp129 ;
	IfZ _tmp133 Goto _L12 ;
	_tmp134 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp134 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L12:
	_tmp135 = 4 ;
	_tmp136 = _tmp126 * _tmp135 ;
	_tmp137 = _tmp136 + _tmp135 ;
	_tmp138 = _tmp125 + _tmp137 ;
	_tmp139 = *(_tmp138) ;
	result = _tmp139 ;
	_tmp140 = *(this + 4) ;
	_tmp141 = 1 ;
	_tmp142 = _tmp140 + _tmp141 ;
	*(this + 4) = _tmp142 ;
	Return result ;
	EndFunc ;
VTable Deck =
	Deck.____Init,
	Deck.____Shuffle,
	Deck.____GetCard,
; 
BJDeck.____Init:
	BeginFunc 220 ;
	_tmp143 = 8 ;
	_tmp144 = 4 ;
	_tmp145 = 0 ;
	_tmp146 = _tmp143 < _tmp145 ;
	_tmp147 = _tmp143 == _tmp145 ;
	_tmp148 = _tmp146 || _tmp147 ;
	IfZ _tmp148 Goto _L13 ;
	_tmp149 = "Decaf runtime error: Array size is <= 0\n" ;
	PushParam _tmp149 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L13:
	_tmp150 = _tmp143 * _tmp144 ;
	_tmp151 = _tmp144 + _tmp150 ;
	PushParam _tmp151 ;
	_tmp152 = LCall _Alloc ;
	PopParams 4 ;
	*(_tmp152) = _tmp143 ;
	*(this + 4) = _tmp152 ;
	_tmp153 = 0 ;
	i = _tmp153 ;
_L14:
	_tmp154 = 8 ;
	_tmp155 = i < _tmp154 ;
	IfZ _tmp155 Goto _L15 ;
	_tmp156 = 8 ;
	_tmp157 = 4 ;
	_tmp158 = _tmp157 + _tmp156 ;
	PushParam _tmp158 ;
	_tmp159 = LCall _Alloc ;
	PopParams 4 ;
	_tmp160 = Deck ;
	*(_tmp159) = _tmp160 ;
	_tmp161 = *(this + 4) ;
	_tmp162 = 0 ;
	_tmp163 = *(_tmp161) ;
	_tmp164 = i < _tmp162 ;
	_tmp165 = _tmp163 < i ;
	_tmp166 = _tmp163 == i ;
	_tmp167 = _tmp165 || _tmp166 ;
	_tmp168 = _tmp167 || _tmp164 ;
	IfZ _tmp168 Goto _L16 ;
	_tmp169 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp169 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L16:
	_tmp170 = 4 ;
	_tmp171 = i * _tmp170 ;
	_tmp172 = _tmp171 + _tmp170 ;
	_tmp173 = _tmp161 + _tmp172 ;
	*(_tmp173) = _tmp159 ;
	_tmp174 = *(_tmp173) ;
	_tmp175 = *(this + 4) ;
	_tmp176 = 0 ;
	_tmp177 = *(_tmp175) ;
	_tmp178 = i < _tmp176 ;
	_tmp179 = _tmp177 < i ;
	_tmp180 = _tmp177 == i ;
	_tmp181 = _tmp179 || _tmp180 ;
	_tmp182 = _tmp181 || _tmp178 ;
	IfZ _tmp182 Goto _L17 ;
	_tmp183 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp183 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L17:
	_tmp184 = 4 ;
	_tmp185 = i * _tmp184 ;
	_tmp186 = _tmp185 + _tmp184 ;
	_tmp187 = _tmp175 + _tmp186 ;
	_tmp188 = *(_tmp187) ;
	PushParam _tmp188 ;
	_tmp189 = *(_tmp188) ;
	_tmp190 = *(_tmp189) ;
	ACall _tmp190 ;
	PopParams 4 ;
	_tmp191 = 1 ;
	_tmp192 = i + _tmp191 ;
	i = _tmp192 ;
	Goto _L14 ;
_L15:
	EndFunc ;
BJDeck.____DealCard:
	BeginFunc 192 ;
	_tmp193 = 0 ;
	c = _tmp193 ;
	_tmp194 = 8 ;
	_tmp195 = 52 ;
	_tmp196 = _tmp194 * _tmp195 ;
	_tmp197 = *(this + 8) ;
	_tmp198 = _tmp196 < _tmp197 ;
	_tmp199 = _tmp196 == _tmp197 ;
	_tmp200 = _tmp198 || _tmp199 ;
	IfZ _tmp200 Goto _L18 ;
	_tmp201 = 11 ;
	Return _tmp201 ;
	Goto _L19 ;
_L18:
_L19:
_L20:
	_tmp202 = 0 ;
	_tmp203 = c == _tmp202 ;
	IfZ _tmp203 Goto _L21 ;
	_tmp204 = 8 ;
	PushParam _tmp204 ;
	PushParam gRnd ;
	_tmp205 = *(gRnd) ;
	_tmp206 = *(_tmp205 + 8) ;
	_tmp207 = ACall _tmp206 ;
	PopParams 8 ;
	d = _tmp207 ;
	_tmp208 = *(this + 4) ;
	_tmp209 = 0 ;
	_tmp210 = *(_tmp208) ;
	_tmp211 = d < _tmp209 ;
	_tmp212 = _tmp210 < d ;
	_tmp213 = _tmp210 == d ;
	_tmp214 = _tmp212 || _tmp213 ;
	_tmp215 = _tmp214 || _tmp211 ;
	IfZ _tmp215 Goto _L22 ;
	_tmp216 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp216 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L22:
	_tmp217 = 4 ;
	_tmp218 = d * _tmp217 ;
	_tmp219 = _tmp218 + _tmp217 ;
	_tmp220 = _tmp208 + _tmp219 ;
	_tmp221 = *(_tmp220) ;
	PushParam _tmp221 ;
	_tmp222 = *(_tmp221) ;
	_tmp223 = *(_tmp222 + 8) ;
	_tmp224 = ACall _tmp223 ;
	PopParams 4 ;
	c = _tmp224 ;
	Goto _L20 ;
_L21:
	_tmp225 = 10 ;
	_tmp226 = _tmp225 < c ;
	IfZ _tmp226 Goto _L23 ;
	_tmp227 = 10 ;
	c = _tmp227 ;
	Goto _L24 ;
_L23:
	_tmp228 = 1 ;
	_tmp229 = c == _tmp228 ;
	IfZ _tmp229 Goto _L25 ;
	_tmp230 = 11 ;
	c = _tmp230 ;
	Goto _L26 ;
_L25:
_L26:
_L24:
	_tmp231 = *(this + 8) ;
	_tmp232 = 1 ;
	_tmp233 = _tmp231 + _tmp232 ;
	*(this + 8) = _tmp233 ;
	Return c ;
	EndFunc ;
BJDeck.____Shuffle:
	BeginFunc 112 ;
	_tmp234 = "Shuffling..." ;
	PushParam _tmp234 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp235 = 0 ;
	i = _tmp235 ;
_L27:
	_tmp236 = 8 ;
	_tmp237 = i < _tmp236 ;
	IfZ _tmp237 Goto _L28 ;
	_tmp238 = *(this + 4) ;
	_tmp239 = 0 ;
	_tmp240 = *(_tmp238) ;
	_tmp241 = i < _tmp239 ;
	_tmp242 = _tmp240 < i ;
	_tmp243 = _tmp240 == i ;
	_tmp244 = _tmp242 || _tmp243 ;
	_tmp245 = _tmp244 || _tmp241 ;
	IfZ _tmp245 Goto _L29 ;
	_tmp246 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp246 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L29:
	_tmp247 = 4 ;
	_tmp248 = i * _tmp247 ;
	_tmp249 = _tmp248 + _tmp247 ;
	_tmp250 = _tmp238 + _tmp249 ;
	_tmp251 = *(_tmp250) ;
	PushParam _tmp251 ;
	_tmp252 = *(_tmp251) ;
	_tmp253 = *(_tmp252 + 4) ;
	ACall _tmp253 ;
	PopParams 4 ;
	_tmp254 = 1 ;
	_tmp255 = i + _tmp254 ;
	i = _tmp255 ;
	Goto _L27 ;
_L28:
	_tmp256 = 0 ;
	*(this + 8) = _tmp256 ;
	_tmp257 = "done.\n" ;
	PushParam _tmp257 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
BJDeck.____NumCardsRemaining:
	BeginFunc 20 ;
	_tmp258 = 8 ;
	_tmp259 = 52 ;
	_tmp260 = _tmp258 * _tmp259 ;
	_tmp261 = *(this + 8) ;
	_tmp262 = _tmp260 - _tmp261 ;
	Return _tmp262 ;
	EndFunc ;
VTable BJDeck =
	BJDeck.____Init,
	BJDeck.____DealCard,
	BJDeck.____Shuffle,
	BJDeck.____NumCardsRemaining,
; 
Player.____Init:
	BeginFunc 16 ;
	_tmp263 = 1000 ;
	*(this + 20) = _tmp263 ;
	_tmp264 = "What is the name of player #" ;
	PushParam _tmp264 ;
	LCall _PrintString ;
	PopParams 4 ;
	PushParam num ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp265 = "? " ;
	PushParam _tmp265 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp266 = LCall _ReadLine ;
	*(this + 24) = _tmp266 ;
	EndFunc ;
Player.____Hit:
	BeginFunc 132 ;
	PushParam deck ;
	_tmp267 = *(deck) ;
	_tmp268 = *(_tmp267 + 4) ;
	_tmp269 = ACall _tmp268 ;
	PopParams 4 ;
	card = _tmp269 ;
	_tmp270 = *(this + 24) ;
	PushParam _tmp270 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp271 = " was dealt a " ;
	PushParam _tmp271 ;
	LCall _PrintString ;
	PopParams 4 ;
	PushParam card ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp272 = ".\n" ;
	PushParam _tmp272 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp273 = *(this + 4) ;
	_tmp274 = _tmp273 + card ;
	*(this + 4) = _tmp274 ;
	_tmp275 = *(this + 12) ;
	_tmp276 = 1 ;
	_tmp277 = _tmp275 + _tmp276 ;
	*(this + 12) = _tmp277 ;
	_tmp278 = 11 ;
	_tmp279 = card == _tmp278 ;
	IfZ _tmp279 Goto _L30 ;
	_tmp280 = *(this + 8) ;
	_tmp281 = 1 ;
	_tmp282 = _tmp280 + _tmp281 ;
	*(this + 8) = _tmp282 ;
	Goto _L31 ;
_L30:
_L31:
_L32:
	_tmp283 = 21 ;
	_tmp284 = *(this + 4) ;
	_tmp285 = _tmp283 < _tmp284 ;
	_tmp286 = 0 ;
	_tmp287 = *(this + 8) ;
	_tmp288 = _tmp286 < _tmp287 ;
	_tmp289 = _tmp285 && _tmp288 ;
	IfZ _tmp289 Goto _L33 ;
	_tmp290 = *(this + 4) ;
	_tmp291 = 10 ;
	_tmp292 = _tmp290 - _tmp291 ;
	*(this + 4) = _tmp292 ;
	_tmp293 = *(this + 8) ;
	_tmp294 = 1 ;
	_tmp295 = _tmp293 - _tmp294 ;
	*(this + 8) = _tmp295 ;
	Goto _L32 ;
_L33:
	EndFunc ;
Player.____DoubleDown:
	BeginFunc 112 ;
	_tmp297 = *(this + 4) ;
	_tmp298 = 10 ;
	_tmp299 = _tmp297 == _tmp298 ;
	IfZ _tmp299 Goto _L37 ;
	_tmp300 = 0 ;
	_tmp296 = _tmp300 ;
	Goto _L36 ;
_L37:
	_tmp301 = 1 ;
	_tmp296 = _tmp301 ;
_L36:
	_tmp303 = *(this + 4) ;
	_tmp304 = 11 ;
	_tmp305 = _tmp303 == _tmp304 ;
	IfZ _tmp305 Goto _L39 ;
	_tmp306 = 0 ;
	_tmp302 = _tmp306 ;
	Goto _L38 ;
_L39:
	_tmp307 = 1 ;
	_tmp302 = _tmp307 ;
_L38:
	_tmp308 = _tmp296 && _tmp302 ;
	IfZ _tmp308 Goto _L34 ;
	_tmp309 = 0 ;
	Return _tmp309 ;
	Goto _L35 ;
_L34:
_L35:
	_tmp310 = "Would you like to double down?" ;
	PushParam _tmp310 ;
	_tmp311 = LCall ____GetYesOrNo ;
	PopParams 4 ;
	IfZ _tmp311 Goto _L40 ;
	_tmp312 = *(this + 16) ;
	_tmp313 = 2 ;
	_tmp314 = _tmp312 * _tmp313 ;
	*(this + 16) = _tmp314 ;
	PushParam deck ;
	PushParam this ;
	_tmp315 = *(this) ;
	_tmp316 = *(_tmp315 + 4) ;
	ACall _tmp316 ;
	PopParams 8 ;
	_tmp317 = *(this + 24) ;
	PushParam _tmp317 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp318 = ", your total is " ;
	PushParam _tmp318 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp319 = *(this + 4) ;
	PushParam _tmp319 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp320 = ".\n" ;
	PushParam _tmp320 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp321 = 1 ;
	Return _tmp321 ;
	Goto _L41 ;
_L40:
_L41:
	_tmp322 = 0 ;
	Return _tmp322 ;
	EndFunc ;
Player.____TakeTurn:
	BeginFunc 180 ;
	_tmp323 = "\n" ;
	PushParam _tmp323 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp324 = *(this + 24) ;
	PushParam _tmp324 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp325 = "'s turn.\n" ;
	PushParam _tmp325 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp326 = 0 ;
	*(this + 4) = _tmp326 ;
	_tmp327 = 0 ;
	*(this + 8) = _tmp327 ;
	_tmp328 = 0 ;
	*(this + 12) = _tmp328 ;
	PushParam deck ;
	PushParam this ;
	_tmp329 = *(this) ;
	_tmp330 = *(_tmp329 + 4) ;
	ACall _tmp330 ;
	PopParams 8 ;
	PushParam deck ;
	PushParam this ;
	_tmp331 = *(this) ;
	_tmp332 = *(_tmp331 + 4) ;
	ACall _tmp332 ;
	PopParams 8 ;
	PushParam deck ;
	PushParam this ;
	_tmp334 = *(this) ;
	_tmp335 = *(_tmp334 + 8) ;
	_tmp336 = ACall _tmp335 ;
	PopParams 8 ;
	IfZ _tmp336 Goto _L45 ;
	_tmp337 = 0 ;
	_tmp333 = _tmp337 ;
	Goto _L44 ;
_L45:
	_tmp338 = 1 ;
	_tmp333 = _tmp338 ;
_L44:
	IfZ _tmp333 Goto _L42 ;
	_tmp339 = 1 ;
	stillGoing = _tmp339 ;
_L46:
	_tmp340 = *(this + 4) ;
	_tmp341 = 21 ;
	_tmp342 = _tmp340 < _tmp341 ;
	_tmp343 = _tmp340 == _tmp341 ;
	_tmp344 = _tmp342 || _tmp343 ;
	_tmp345 = _tmp344 && stillGoing ;
	IfZ _tmp345 Goto _L47 ;
	_tmp346 = *(this + 24) ;
	PushParam _tmp346 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp347 = ", your total is " ;
	PushParam _tmp347 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp348 = *(this + 4) ;
	PushParam _tmp348 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp349 = ".\n" ;
	PushParam _tmp349 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp350 = "Would you like a hit?" ;
	PushParam _tmp350 ;
	_tmp351 = LCall ____GetYesOrNo ;
	PopParams 4 ;
	stillGoing = _tmp351 ;
	IfZ stillGoing Goto _L48 ;
	PushParam deck ;
	PushParam this ;
	_tmp352 = *(this) ;
	_tmp353 = *(_tmp352 + 4) ;
	ACall _tmp353 ;
	PopParams 8 ;
	Goto _L49 ;
_L48:
_L49:
	Goto _L46 ;
_L47:
	Goto _L43 ;
_L42:
_L43:
	_tmp354 = 21 ;
	_tmp355 = *(this + 4) ;
	_tmp356 = _tmp354 < _tmp355 ;
	IfZ _tmp356 Goto _L50 ;
	_tmp357 = *(this + 24) ;
	PushParam _tmp357 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp358 = " busts with the big " ;
	PushParam _tmp358 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp359 = *(this + 4) ;
	PushParam _tmp359 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp360 = "!\n" ;
	PushParam _tmp360 ;
	LCall _PrintString ;
	PopParams 4 ;
	Goto _L51 ;
_L50:
	_tmp361 = *(this + 24) ;
	PushParam _tmp361 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp362 = " stays at " ;
	PushParam _tmp362 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp363 = *(this + 4) ;
	PushParam _tmp363 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp364 = ".\n" ;
	PushParam _tmp364 ;
	LCall _PrintString ;
	PopParams 4 ;
_L51:
	EndFunc ;
Player.____HasMoney:
	BeginFunc 12 ;
	_tmp365 = 0 ;
	_tmp366 = *(this + 20) ;
	_tmp367 = _tmp365 < _tmp366 ;
	Return _tmp367 ;
	EndFunc ;
Player.____PrintMoney:
	BeginFunc 16 ;
	_tmp368 = *(this + 24) ;
	PushParam _tmp368 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp369 = ", you have $" ;
	PushParam _tmp369 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp370 = *(this + 20) ;
	PushParam _tmp370 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp371 = ".\n" ;
	PushParam _tmp371 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
Player.____PlaceBet:
	BeginFunc 56 ;
	_tmp372 = 0 ;
	*(this + 16) = _tmp372 ;
	PushParam this ;
	_tmp373 = *(this) ;
	_tmp374 = *(_tmp373 + 20) ;
	ACall _tmp374 ;
	PopParams 4 ;
_L52:
	_tmp375 = *(this + 16) ;
	_tmp376 = 0 ;
	_tmp377 = _tmp375 < _tmp376 ;
	_tmp378 = _tmp375 == _tmp376 ;
	_tmp379 = _tmp377 || _tmp378 ;
	_tmp380 = *(this + 20) ;
	_tmp381 = *(this + 16) ;
	_tmp382 = _tmp380 < _tmp381 ;
	_tmp383 = _tmp379 || _tmp382 ;
	IfZ _tmp383 Goto _L53 ;
	_tmp384 = "How much would you like to bet? " ;
	PushParam _tmp384 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp385 = LCall _ReadInteger ;
	*(this + 16) = _tmp385 ;
	Goto _L52 ;
_L53:
	EndFunc ;
Player.____GetTotal:
	BeginFunc 4 ;
	_tmp386 = *(this + 4) ;
	Return _tmp386 ;
	EndFunc ;
Player.____Resolve:
	BeginFunc 224 ;
	_tmp387 = 0 ;
	win = _tmp387 ;
	_tmp388 = 0 ;
	lose = _tmp388 ;
	_tmp389 = *(this + 4) ;
	_tmp390 = 21 ;
	_tmp391 = _tmp389 == _tmp390 ;
	_tmp392 = *(this + 12) ;
	_tmp393 = 2 ;
	_tmp394 = _tmp392 == _tmp393 ;
	_tmp395 = _tmp391 && _tmp394 ;
	IfZ _tmp395 Goto _L54 ;
	_tmp396 = 2 ;
	win = _tmp396 ;
	Goto _L55 ;
_L54:
	_tmp397 = 21 ;
	_tmp398 = *(this + 4) ;
	_tmp399 = _tmp397 < _tmp398 ;
	IfZ _tmp399 Goto _L56 ;
	_tmp400 = 1 ;
	lose = _tmp400 ;
	Goto _L57 ;
_L56:
	_tmp401 = 21 ;
	_tmp402 = _tmp401 < dealer ;
	IfZ _tmp402 Goto _L58 ;
	_tmp403 = 1 ;
	win = _tmp403 ;
	Goto _L59 ;
_L58:
	_tmp404 = *(this + 4) ;
	_tmp405 = dealer < _tmp404 ;
	IfZ _tmp405 Goto _L60 ;
	_tmp406 = 1 ;
	win = _tmp406 ;
	Goto _L61 ;
_L60:
	_tmp407 = *(this + 4) ;
	_tmp408 = _tmp407 < dealer ;
	IfZ _tmp408 Goto _L62 ;
	_tmp409 = 1 ;
	lose = _tmp409 ;
	Goto _L63 ;
_L62:
_L63:
_L61:
_L59:
_L57:
_L55:
	_tmp410 = 1 ;
	_tmp411 = _tmp410 < win ;
	_tmp412 = _tmp410 == win ;
	_tmp413 = _tmp411 || _tmp412 ;
	IfZ _tmp413 Goto _L64 ;
	_tmp414 = *(this + 24) ;
	PushParam _tmp414 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp415 = ", you won $" ;
	PushParam _tmp415 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp416 = *(this + 16) ;
	PushParam _tmp416 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp417 = ".\n" ;
	PushParam _tmp417 ;
	LCall _PrintString ;
	PopParams 4 ;
	Goto _L65 ;
_L64:
	_tmp418 = 1 ;
	_tmp419 = _tmp418 < lose ;
	_tmp420 = _tmp418 == lose ;
	_tmp421 = _tmp419 || _tmp420 ;
	IfZ _tmp421 Goto _L66 ;
	_tmp422 = *(this + 24) ;
	PushParam _tmp422 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp423 = ", you lost $" ;
	PushParam _tmp423 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp424 = *(this + 16) ;
	PushParam _tmp424 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp425 = ".\n" ;
	PushParam _tmp425 ;
	LCall _PrintString ;
	PopParams 4 ;
	Goto _L67 ;
_L66:
	_tmp426 = *(this + 24) ;
	PushParam _tmp426 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp427 = ", you push!\n" ;
	PushParam _tmp427 ;
	LCall _PrintString ;
	PopParams 4 ;
_L67:
_L65:
	_tmp428 = *(this + 16) ;
	_tmp429 = win * _tmp428 ;
	win = _tmp429 ;
	_tmp430 = *(this + 16) ;
	_tmp431 = lose * _tmp430 ;
	lose = _tmp431 ;
	_tmp432 = *(this + 20) ;
	_tmp433 = _tmp432 + win ;
	_tmp434 = _tmp433 - lose ;
	*(this + 20) = _tmp434 ;
	EndFunc ;
VTable Player =
	Player.____Init,
	Player.____Hit,
	Player.____DoubleDown,
	Player.____TakeTurn,
	Player.____HasMoney,
	Player.____PrintMoney,
	Player.____PlaceBet,
	Player.____GetTotal,
	Player.____Resolve,
; 
Dealer.____Init:
	BeginFunc 16 ;
	_tmp435 = 0 ;
	*(this + 4) = _tmp435 ;
	_tmp436 = 0 ;
	*(this + 8) = _tmp436 ;
	_tmp437 = 0 ;
	*(this + 12) = _tmp437 ;
	_tmp438 = "Dealer" ;
	*(this + 24) = _tmp438 ;
	EndFunc ;
Dealer.____TakeTurn:
	BeginFunc 84 ;
	_tmp439 = "\n" ;
	PushParam _tmp439 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp440 = *(this + 24) ;
	PushParam _tmp440 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp441 = "'s turn.\n" ;
	PushParam _tmp441 ;
	LCall _PrintString ;
	PopParams 4 ;
_L68:
	_tmp442 = *(this + 4) ;
	_tmp443 = 16 ;
	_tmp444 = _tmp442 < _tmp443 ;
	_tmp445 = _tmp442 == _tmp443 ;
	_tmp446 = _tmp444 || _tmp445 ;
	IfZ _tmp446 Goto _L69 ;
	PushParam deck ;
	PushParam this ;
	_tmp447 = *(this) ;
	_tmp448 = *(_tmp447 + 4) ;
	ACall _tmp448 ;
	PopParams 8 ;
	Goto _L68 ;
_L69:
	_tmp449 = 21 ;
	_tmp450 = *(this + 4) ;
	_tmp451 = _tmp449 < _tmp450 ;
	IfZ _tmp451 Goto _L70 ;
	_tmp452 = *(this + 24) ;
	PushParam _tmp452 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp453 = " busts with the big " ;
	PushParam _tmp453 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp454 = *(this + 4) ;
	PushParam _tmp454 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp455 = "!\n" ;
	PushParam _tmp455 ;
	LCall _PrintString ;
	PopParams 4 ;
	Goto _L71 ;
_L70:
	_tmp456 = *(this + 24) ;
	PushParam _tmp456 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp457 = " stays at " ;
	PushParam _tmp457 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp458 = *(this + 4) ;
	PushParam _tmp458 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp459 = ".\n" ;
	PushParam _tmp459 ;
	LCall _PrintString ;
	PopParams 4 ;
_L71:
	EndFunc ;
VTable Dealer =
	Dealer.____Init,
	Player.____Hit,
	Player.____DoubleDown,
	Dealer.____TakeTurn,
	Player.____HasMoney,
	Player.____PrintMoney,
	Player.____PlaceBet,
	Player.____GetTotal,
	Player.____Resolve,
	Dealer.____Init,
	Dealer.____TakeTurn,
; 
House.____SetupGame:
	BeginFunc 108 ;
	_tmp460 = "\nWelcome to CS143 BlackJack!\n" ;
	PushParam _tmp460 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp461 = "---------------------------\n" ;
	PushParam _tmp461 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp462 = 4 ;
	_tmp463 = 4 ;
	_tmp464 = _tmp463 + _tmp462 ;
	PushParam _tmp464 ;
	_tmp465 = LCall _Alloc ;
	PopParams 4 ;
	_tmp466 = Random ;
	*(_tmp465) = _tmp466 ;
	gRnd = _tmp465 ;
	_tmp467 = "Please enter a random number seed: " ;
	PushParam _tmp467 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp468 = LCall _ReadInteger ;
	PushParam _tmp468 ;
	PushParam gRnd ;
	_tmp469 = *(gRnd) ;
	_tmp470 = *(_tmp469) ;
	ACall _tmp470 ;
	PopParams 8 ;
	_tmp471 = 8 ;
	_tmp472 = 4 ;
	_tmp473 = _tmp472 + _tmp471 ;
	PushParam _tmp473 ;
	_tmp474 = LCall _Alloc ;
	PopParams 4 ;
	_tmp475 = BJDeck ;
	*(_tmp474) = _tmp475 ;
	*(this + 12) = _tmp474 ;
	_tmp476 = 24 ;
	_tmp477 = 4 ;
	_tmp478 = _tmp477 + _tmp476 ;
	PushParam _tmp478 ;
	_tmp479 = LCall _Alloc ;
	PopParams 4 ;
	_tmp480 = Dealer ;
	*(_tmp479) = _tmp480 ;
	*(this + 8) = _tmp479 ;
	_tmp481 = *(this + 12) ;
	PushParam _tmp481 ;
	_tmp482 = *(_tmp481) ;
	_tmp483 = *(_tmp482) ;
	ACall _tmp483 ;
	PopParams 4 ;
	_tmp484 = *(this + 12) ;
	PushParam _tmp484 ;
	_tmp485 = *(_tmp484) ;
	_tmp486 = *(_tmp485 + 8) ;
	ACall _tmp486 ;
	PopParams 4 ;
	EndFunc ;
House.____SetupPlayers:
	BeginFunc 248 ;
	_tmp487 = "How many players do we have today? " ;
	PushParam _tmp487 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp488 = LCall _ReadInteger ;
	numPlayers = _tmp488 ;
	_tmp489 = 4 ;
	_tmp490 = 0 ;
	_tmp491 = numPlayers < _tmp490 ;
	_tmp492 = numPlayers == _tmp490 ;
	_tmp493 = _tmp491 || _tmp492 ;
	IfZ _tmp493 Goto _L72 ;
	_tmp494 = "Decaf runtime error: Array size is <= 0\n" ;
	PushParam _tmp494 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L72:
	_tmp495 = numPlayers * _tmp489 ;
	_tmp496 = _tmp489 + _tmp495 ;
	PushParam _tmp496 ;
	_tmp497 = LCall _Alloc ;
	PopParams 4 ;
	*(_tmp497) = numPlayers ;
	*(this + 4) = _tmp497 ;
	_tmp498 = 0 ;
	i = _tmp498 ;
_L73:
	_tmp499 = *(this + 4) ;
	_tmp500 = *(_tmp499) ;
	_tmp501 = i < _tmp500 ;
	IfZ _tmp501 Goto _L74 ;
	_tmp502 = 24 ;
	_tmp503 = 4 ;
	_tmp504 = _tmp503 + _tmp502 ;
	PushParam _tmp504 ;
	_tmp505 = LCall _Alloc ;
	PopParams 4 ;
	_tmp506 = Player ;
	*(_tmp505) = _tmp506 ;
	_tmp507 = *(this + 4) ;
	_tmp508 = 0 ;
	_tmp509 = *(_tmp507) ;
	_tmp510 = i < _tmp508 ;
	_tmp511 = _tmp509 < i ;
	_tmp512 = _tmp509 == i ;
	_tmp513 = _tmp511 || _tmp512 ;
	_tmp514 = _tmp513 || _tmp510 ;
	IfZ _tmp514 Goto _L75 ;
	_tmp515 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp515 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L75:
	_tmp516 = 4 ;
	_tmp517 = i * _tmp516 ;
	_tmp518 = _tmp517 + _tmp516 ;
	_tmp519 = _tmp507 + _tmp518 ;
	*(_tmp519) = _tmp505 ;
	_tmp520 = *(_tmp519) ;
	_tmp521 = 1 ;
	_tmp522 = i + _tmp521 ;
	PushParam _tmp522 ;
	_tmp523 = *(this + 4) ;
	_tmp524 = 0 ;
	_tmp525 = *(_tmp523) ;
	_tmp526 = i < _tmp524 ;
	_tmp527 = _tmp525 < i ;
	_tmp528 = _tmp525 == i ;
	_tmp529 = _tmp527 || _tmp528 ;
	_tmp530 = _tmp529 || _tmp526 ;
	IfZ _tmp530 Goto _L76 ;
	_tmp531 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp531 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L76:
	_tmp532 = 4 ;
	_tmp533 = i * _tmp532 ;
	_tmp534 = _tmp533 + _tmp532 ;
	_tmp535 = _tmp523 + _tmp534 ;
	_tmp536 = *(_tmp535) ;
	PushParam _tmp536 ;
	_tmp537 = *(_tmp536) ;
	_tmp538 = *(_tmp537) ;
	ACall _tmp538 ;
	PopParams 8 ;
	_tmp539 = 1 ;
	_tmp540 = i + _tmp539 ;
	i = _tmp540 ;
	Goto _L73 ;
_L74:
	EndFunc ;
House.____TakeAllBets:
	BeginFunc 180 ;
	_tmp541 = "\nFirst, let's take bets.\n" ;
	PushParam _tmp541 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp542 = 0 ;
	i = _tmp542 ;
_L77:
	_tmp543 = *(this + 4) ;
	_tmp544 = *(_tmp543) ;
	_tmp545 = i < _tmp544 ;
	IfZ _tmp545 Goto _L78 ;
	_tmp546 = *(this + 4) ;
	_tmp547 = 0 ;
	_tmp548 = *(_tmp546) ;
	_tmp549 = i < _tmp547 ;
	_tmp550 = _tmp548 < i ;
	_tmp551 = _tmp548 == i ;
	_tmp552 = _tmp550 || _tmp551 ;
	_tmp553 = _tmp552 || _tmp549 ;
	IfZ _tmp553 Goto _L81 ;
	_tmp554 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp554 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L81:
	_tmp555 = 4 ;
	_tmp556 = i * _tmp555 ;
	_tmp557 = _tmp556 + _tmp555 ;
	_tmp558 = _tmp546 + _tmp557 ;
	_tmp559 = *(_tmp558) ;
	PushParam _tmp559 ;
	_tmp560 = *(_tmp559) ;
	_tmp561 = *(_tmp560 + 16) ;
	_tmp562 = ACall _tmp561 ;
	PopParams 4 ;
	IfZ _tmp562 Goto _L79 ;
	_tmp563 = *(this + 4) ;
	_tmp564 = 0 ;
	_tmp565 = *(_tmp563) ;
	_tmp566 = i < _tmp564 ;
	_tmp567 = _tmp565 < i ;
	_tmp568 = _tmp565 == i ;
	_tmp569 = _tmp567 || _tmp568 ;
	_tmp570 = _tmp569 || _tmp566 ;
	IfZ _tmp570 Goto _L82 ;
	_tmp571 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp571 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L82:
	_tmp572 = 4 ;
	_tmp573 = i * _tmp572 ;
	_tmp574 = _tmp573 + _tmp572 ;
	_tmp575 = _tmp563 + _tmp574 ;
	_tmp576 = *(_tmp575) ;
	PushParam _tmp576 ;
	_tmp577 = *(_tmp576) ;
	_tmp578 = *(_tmp577 + 24) ;
	ACall _tmp578 ;
	PopParams 4 ;
	Goto _L80 ;
_L79:
_L80:
	_tmp579 = 1 ;
	_tmp580 = i + _tmp579 ;
	i = _tmp580 ;
	Goto _L77 ;
_L78:
	EndFunc ;
House.____TakeAllTurns:
	BeginFunc 180 ;
	_tmp581 = 0 ;
	i = _tmp581 ;
_L83:
	_tmp582 = *(this + 4) ;
	_tmp583 = *(_tmp582) ;
	_tmp584 = i < _tmp583 ;
	IfZ _tmp584 Goto _L84 ;
	_tmp585 = *(this + 4) ;
	_tmp586 = 0 ;
	_tmp587 = *(_tmp585) ;
	_tmp588 = i < _tmp586 ;
	_tmp589 = _tmp587 < i ;
	_tmp590 = _tmp587 == i ;
	_tmp591 = _tmp589 || _tmp590 ;
	_tmp592 = _tmp591 || _tmp588 ;
	IfZ _tmp592 Goto _L87 ;
	_tmp593 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp593 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L87:
	_tmp594 = 4 ;
	_tmp595 = i * _tmp594 ;
	_tmp596 = _tmp595 + _tmp594 ;
	_tmp597 = _tmp585 + _tmp596 ;
	_tmp598 = *(_tmp597) ;
	PushParam _tmp598 ;
	_tmp599 = *(_tmp598) ;
	_tmp600 = *(_tmp599 + 16) ;
	_tmp601 = ACall _tmp600 ;
	PopParams 4 ;
	IfZ _tmp601 Goto _L85 ;
	_tmp602 = *(this + 12) ;
	PushParam _tmp602 ;
	_tmp603 = *(this + 4) ;
	_tmp604 = 0 ;
	_tmp605 = *(_tmp603) ;
	_tmp606 = i < _tmp604 ;
	_tmp607 = _tmp605 < i ;
	_tmp608 = _tmp605 == i ;
	_tmp609 = _tmp607 || _tmp608 ;
	_tmp610 = _tmp609 || _tmp606 ;
	IfZ _tmp610 Goto _L88 ;
	_tmp611 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp611 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L88:
	_tmp612 = 4 ;
	_tmp613 = i * _tmp612 ;
	_tmp614 = _tmp613 + _tmp612 ;
	_tmp615 = _tmp603 + _tmp614 ;
	_tmp616 = *(_tmp615) ;
	PushParam _tmp616 ;
	_tmp617 = *(_tmp616) ;
	_tmp618 = *(_tmp617 + 12) ;
	ACall _tmp618 ;
	PopParams 8 ;
	Goto _L86 ;
_L85:
_L86:
	_tmp619 = 1 ;
	_tmp620 = i + _tmp619 ;
	i = _tmp620 ;
	Goto _L83 ;
_L84:
	EndFunc ;
House.____ResolveAllPlayers:
	BeginFunc 196 ;
	_tmp621 = "\nTime to resolve bets.\n" ;
	PushParam _tmp621 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp622 = 0 ;
	i = _tmp622 ;
_L89:
	_tmp623 = *(this + 4) ;
	_tmp624 = *(_tmp623) ;
	_tmp625 = i < _tmp624 ;
	IfZ _tmp625 Goto _L90 ;
	_tmp626 = *(this + 4) ;
	_tmp627 = 0 ;
	_tmp628 = *(_tmp626) ;
	_tmp629 = i < _tmp627 ;
	_tmp630 = _tmp628 < i ;
	_tmp631 = _tmp628 == i ;
	_tmp632 = _tmp630 || _tmp631 ;
	_tmp633 = _tmp632 || _tmp629 ;
	IfZ _tmp633 Goto _L93 ;
	_tmp634 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp634 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L93:
	_tmp635 = 4 ;
	_tmp636 = i * _tmp635 ;
	_tmp637 = _tmp636 + _tmp635 ;
	_tmp638 = _tmp626 + _tmp637 ;
	_tmp639 = *(_tmp638) ;
	PushParam _tmp639 ;
	_tmp640 = *(_tmp639) ;
	_tmp641 = *(_tmp640 + 16) ;
	_tmp642 = ACall _tmp641 ;
	PopParams 4 ;
	IfZ _tmp642 Goto _L91 ;
	_tmp643 = *(this + 8) ;
	PushParam _tmp643 ;
	_tmp644 = *(_tmp643) ;
	_tmp645 = *(_tmp644 + 28) ;
	_tmp646 = ACall _tmp645 ;
	PopParams 4 ;
	PushParam _tmp646 ;
	_tmp647 = *(this + 4) ;
	_tmp648 = 0 ;
	_tmp649 = *(_tmp647) ;
	_tmp650 = i < _tmp648 ;
	_tmp651 = _tmp649 < i ;
	_tmp652 = _tmp649 == i ;
	_tmp653 = _tmp651 || _tmp652 ;
	_tmp654 = _tmp653 || _tmp650 ;
	IfZ _tmp654 Goto _L94 ;
	_tmp655 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp655 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L94:
	_tmp656 = 4 ;
	_tmp657 = i * _tmp656 ;
	_tmp658 = _tmp657 + _tmp656 ;
	_tmp659 = _tmp647 + _tmp658 ;
	_tmp660 = *(_tmp659) ;
	PushParam _tmp660 ;
	_tmp661 = *(_tmp660) ;
	_tmp662 = *(_tmp661 + 32) ;
	ACall _tmp662 ;
	PopParams 8 ;
	Goto _L92 ;
_L91:
_L92:
	_tmp663 = 1 ;
	_tmp664 = i + _tmp663 ;
	i = _tmp664 ;
	Goto _L89 ;
_L90:
	EndFunc ;
House.____PrintAllMoney:
	BeginFunc 104 ;
	_tmp665 = 0 ;
	i = _tmp665 ;
_L95:
	_tmp666 = *(this + 4) ;
	_tmp667 = *(_tmp666) ;
	_tmp668 = i < _tmp667 ;
	IfZ _tmp668 Goto _L96 ;
	_tmp669 = *(this + 4) ;
	_tmp670 = 0 ;
	_tmp671 = *(_tmp669) ;
	_tmp672 = i < _tmp670 ;
	_tmp673 = _tmp671 < i ;
	_tmp674 = _tmp671 == i ;
	_tmp675 = _tmp673 || _tmp674 ;
	_tmp676 = _tmp675 || _tmp672 ;
	IfZ _tmp676 Goto _L97 ;
	_tmp677 = "Decaf runtime error: Array subscript out of bound..." ;
	PushParam _tmp677 ;
	LCall _PrintString ;
	PopParams 4 ;
	LCall _Halt ;
_L97:
	_tmp678 = 4 ;
	_tmp679 = i * _tmp678 ;
	_tmp680 = _tmp679 + _tmp678 ;
	_tmp681 = _tmp669 + _tmp680 ;
	_tmp682 = *(_tmp681) ;
	PushParam _tmp682 ;
	_tmp683 = *(_tmp682) ;
	_tmp684 = *(_tmp683 + 20) ;
	ACall _tmp684 ;
	PopParams 4 ;
	_tmp685 = 1 ;
	_tmp686 = i + _tmp685 ;
	i = _tmp686 ;
	Goto _L95 ;
_L96:
	EndFunc ;
House.____PlayOneGame:
	BeginFunc 112 ;
	_tmp687 = *(this + 12) ;
	PushParam _tmp687 ;
	_tmp688 = *(_tmp687) ;
	_tmp689 = *(_tmp688 + 12) ;
	_tmp690 = ACall _tmp689 ;
	PopParams 4 ;
	_tmp691 = 26 ;
	_tmp692 = _tmp690 < _tmp691 ;
	IfZ _tmp692 Goto _L98 ;
	_tmp693 = *(this + 12) ;
	PushParam _tmp693 ;
	_tmp694 = *(_tmp693) ;
	_tmp695 = *(_tmp694 + 8) ;
	ACall _tmp695 ;
	PopParams 4 ;
	Goto _L99 ;
_L98:
_L99:
	PushParam this ;
	_tmp696 = *(this) ;
	_tmp697 = *(_tmp696 + 8) ;
	ACall _tmp697 ;
	PopParams 4 ;
	_tmp698 = "\nDealer starts. " ;
	PushParam _tmp698 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp699 = 0 ;
	PushParam _tmp699 ;
	_tmp700 = *(this + 8) ;
	PushParam _tmp700 ;
	_tmp701 = *(_tmp700) ;
	_tmp702 = *(_tmp701) ;
	ACall _tmp702 ;
	PopParams 8 ;
	_tmp703 = *(this + 12) ;
	PushParam _tmp703 ;
	_tmp704 = *(this + 8) ;
	PushParam _tmp704 ;
	_tmp705 = *(_tmp704) ;
	_tmp706 = *(_tmp705 + 4) ;
	ACall _tmp706 ;
	PopParams 8 ;
	PushParam this ;
	_tmp707 = *(this) ;
	_tmp708 = *(_tmp707 + 12) ;
	ACall _tmp708 ;
	PopParams 4 ;
	_tmp709 = *(this + 12) ;
	PushParam _tmp709 ;
	_tmp710 = *(this + 8) ;
	PushParam _tmp710 ;
	_tmp711 = *(_tmp710) ;
	_tmp712 = *(_tmp711 + 12) ;
	ACall _tmp712 ;
	PopParams 8 ;
	PushParam this ;
	_tmp713 = *(this) ;
	_tmp714 = *(_tmp713 + 16) ;
	ACall _tmp714 ;
	PopParams 4 ;
	EndFunc ;
VTable House =
	House.____SetupGame,
	House.____SetupPlayers,
	House.____TakeAllBets,
	House.____TakeAllTurns,
	House.____ResolveAllPlayers,
	House.____PrintAllMoney,
	House.____PlayOneGame,
; 
____GetYesOrNo:
	BeginFunc 40 ;
	PushParam prompt ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp715 = " (y/n) " ;
	PushParam _tmp715 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp716 = LCall _ReadLine ;
	answer = _tmp716 ;
	_tmp717 = "y" ;
	PushParam _tmp717 ;
	PushParam answer ;
	_tmp718 = LCall _StringEqual ;
	PopParams 8 ;
	_tmp719 = "Y" ;
	PushParam _tmp719 ;
	PushParam answer ;
	_tmp720 = LCall _StringEqual ;
	PopParams 8 ;
	_tmp721 = _tmp718 || _tmp720 ;
	Return _tmp721 ;
	EndFunc ;
main:
	BeginFunc 104 ;
	_tmp722 = 1 ;
	keepPlaying = _tmp722 ;
	_tmp723 = 12 ;
	_tmp724 = 4 ;
	_tmp725 = _tmp724 + _tmp723 ;
	PushParam _tmp725 ;
	_tmp726 = LCall _Alloc ;
	PopParams 4 ;
	_tmp727 = House ;
	*(_tmp726) = _tmp727 ;
	house = _tmp726 ;
	PushParam house ;
	_tmp728 = *(house) ;
	_tmp729 = *(_tmp728) ;
	ACall _tmp729 ;
	PopParams 4 ;
	PushParam house ;
	_tmp730 = *(house) ;
	_tmp731 = *(_tmp730 + 4) ;
	ACall _tmp731 ;
	PopParams 4 ;
_L100:
	IfZ keepPlaying Goto _L101 ;
	PushParam house ;
	_tmp732 = *(house) ;
	_tmp733 = *(_tmp732 + 24) ;
	ACall _tmp733 ;
	PopParams 4 ;
	_tmp734 = "\nDo you want to play another hand?" ;
	PushParam _tmp734 ;
	_tmp735 = LCall ____GetYesOrNo ;
	PopParams 4 ;
	keepPlaying = _tmp735 ;
	Goto _L100 ;
_L101:
	PushParam house ;
	_tmp736 = *(house) ;
	_tmp737 = *(_tmp736 + 20) ;
	ACall _tmp737 ;
	PopParams 4 ;
	_tmp738 = "Thank you for playing...come again soon.\n" ;
	PushParam _tmp738 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp739 = "\nCS143 BlackJack Copyright (c) 1999 by Peter Mor..." ;
	PushParam _tmp739 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp740 = "(2001 mods by jdz)\n" ;
	PushParam _tmp740 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
